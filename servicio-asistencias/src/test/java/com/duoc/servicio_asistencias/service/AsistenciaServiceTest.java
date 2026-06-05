package com.duoc.servicio_asistencias.service;

import com.duoc.servicio_asistencias.dto.AsistenciaDTO;
import com.duoc.servicio_asistencias.dto.AsistenciaRequest;
import com.duoc.servicio_asistencias.exception.AsistenciaNotFoundException;
import com.duoc.servicio_asistencias.model.Asistencia;
import com.duoc.servicio_asistencias.repository.AsistenciaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AsistenciaServiceTest {

    @Mock
    private AsistenciaRepository repository;

    @InjectMocks
    private AsistenciaService asistenciaService;

    @Test
    void guardar_deberiaCrearYRetornarDTO() {
        AsistenciaRequest request = new AsistenciaRequest();
        request.setEstudianteId(1L);
        request.setCursoId(2L);
        request.setFecha(LocalDate.of(2026, 6, 3));
        request.setPresente(true);

        Asistencia saved = new Asistencia();
        saved.setId(1L);
        saved.setEstudianteId(1L);
        saved.setCursoId(2L);
        saved.setFecha(LocalDate.of(2026, 6, 3));
        saved.setPresente(true);

        when(repository.save(any(Asistencia.class))).thenReturn(saved);

        AsistenciaDTO result = asistenciaService.guardar(request);

        assertEquals(1L, result.getId());
        assertEquals("asistencia", result.getCategoria());
    }

    @Test
    void listar_deberiaRetornarTodasLasAsistencias() {
        Asistencia a = new Asistencia();
        a.setId(1L);
        when(repository.findAll()).thenReturn(List.of(a));

        List<AsistenciaDTO> result = asistenciaService.listar();

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
    }

    @Test
    void buscarPorId_deberiaRetornarDTOCuandoExiste() {
        Asistencia a = new Asistencia();
        a.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(a));

        AsistenciaDTO result = asistenciaService.buscarPorId(1L);

        assertEquals(1L, result.getId());
    }

    @Test
    void buscarPorId_deberiaLanzarExcepcionCuandoNoExiste() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(AsistenciaNotFoundException.class, () -> asistenciaService.buscarPorId(1L));
    }

    @Test
    void actualizar_deberiaActualizarYRetornarDTO() {
        Asistencia existente = new Asistencia();
        existente.setId(1L);
        existente.setEstudianteId(2L);
        existente.setCursoId(3L);

        AsistenciaRequest request = new AsistenciaRequest();
        request.setEstudianteId(9L);
        request.setCursoId(8L);
        request.setFecha(LocalDate.of(2026, 6, 4));
        request.setPresente(false);

        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(existente)).thenReturn(existente);

        AsistenciaDTO result = asistenciaService.actualizar(1L, request);

        assertEquals(9L, result.getEstudianteId());
        verify(repository).save(existente);
    }

    @Test
    void eliminar_deberiaBorrarSiExiste() {
        Asistencia existente = new Asistencia();
        existente.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(existente));

        asistenciaService.eliminar(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void eliminar_deberiaLanzarExcepcionSiNoExiste() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(AsistenciaNotFoundException.class, () -> asistenciaService.eliminar(1L));
        verify(repository, never()).deleteById(any());
    }
}