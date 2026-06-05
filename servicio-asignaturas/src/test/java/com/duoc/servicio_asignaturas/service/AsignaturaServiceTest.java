package com.duoc.servicio_asignaturas.service;

import com.duoc.servicio_asignaturas.dto.AsignaturaDTO;
import com.duoc.servicio_asignaturas.dto.AsignaturaRequest;
import com.duoc.servicio_asignaturas.exception.AsignaturaNotFoundException;
import com.duoc.servicio_asignaturas.model.Asignatura;
import com.duoc.servicio_asignaturas.repository.AsignaturaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AsignaturaServiceTest {

    @Mock
    private AsignaturaRepository repository;

    @InjectMocks
    private AsignaturaService asignaturaService;

    @Test
    void guardar_deberiaCrearYRetornarDTO() {
        AsignaturaRequest request = new AsignaturaRequest();
        request.setNombre("Matematica");
        request.setCodigo("MAT101");

        Asignatura saved = new Asignatura();
        saved.setId(1L);
        saved.setNombre("Matematica");
        saved.setCodigo("MAT101");

        when(repository.save(any(Asignatura.class))).thenReturn(saved);

        AsignaturaDTO result = asignaturaService.guardar(request);

        assertEquals(1L, result.getId());
        assertEquals("asignatura", result.getCategoria());
        assertEquals("MAT101", result.getCodigo());
    }

    @Test
    void listar_deberiaRetornarTodasLasAsignaturas() {
        Asignatura a1 = new Asignatura();
        a1.setId(1L);
        a1.setNombre("Matematica");
        Asignatura a2 = new Asignatura();
        a2.setId(2L);
        a2.setNombre("Lenguaje");

        when(repository.findAll()).thenReturn(List.of(a1, a2));

        List<AsignaturaDTO> result = asignaturaService.listar();

        assertEquals(2, result.size());
        assertEquals("Matematica", result.get(0).getNombre());
    }

    @Test
    void buscarPorId_deberiaRetornarDTOCuandoExiste() {
        Asignatura asignatura = new Asignatura();
        asignatura.setId(3L);
        asignatura.setCodigo("HIS101");

        when(repository.findById(3L)).thenReturn(Optional.of(asignatura));

        AsignaturaDTO result = asignaturaService.buscarPorId(3L);

        assertEquals(3L, result.getId());
        assertEquals("HIS101", result.getCodigo());
    }

    @Test
    void buscarPorId_deberiaLanzarExcepcionCuandoNoExiste() {
        when(repository.findById(3L)).thenReturn(Optional.empty());

        assertThrows(AsignaturaNotFoundException.class, () -> asignaturaService.buscarPorId(3L));
    }

    @Test
    void actualizar_deberiaActualizarYRetornarDTO() {
        Asignatura existente = new Asignatura();
        existente.setId(3L);
        existente.setNombre("Historia");
        existente.setCodigo("HIS101");

        AsignaturaRequest request = new AsignaturaRequest();
        request.setNombre("Historia Universal");
        request.setCodigo("HIS102");

        when(repository.findById(3L)).thenReturn(Optional.of(existente));
        when(repository.save(existente)).thenReturn(existente);

        AsignaturaDTO result = asignaturaService.actualizar(3L, request);

        assertEquals("HIS102", result.getCodigo());
        verify(repository).save(existente);
    }

    @Test
    void eliminar_deberiaBorrarSiExiste() {
        Asignatura existente = new Asignatura();
        existente.setId(3L);
        when(repository.findById(3L)).thenReturn(Optional.of(existente));

        asignaturaService.eliminar(3L);

        verify(repository).deleteById(3L);
    }

    @Test
    void eliminar_deberiaLanzarExcepcionSiNoExiste() {
        when(repository.findById(3L)).thenReturn(Optional.empty());

        assertThrows(AsignaturaNotFoundException.class, () -> asignaturaService.eliminar(3L));
        verify(repository, never()).deleteById(any());
    }
}