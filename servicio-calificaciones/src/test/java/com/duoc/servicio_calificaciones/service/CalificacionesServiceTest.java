package com.duoc.servicio_calificaciones.service;

import com.duoc.servicio_calificaciones.dto.CalificacionDTO;
import com.duoc.servicio_calificaciones.dto.CalificacionRequest;
import com.duoc.servicio_calificaciones.exception.CalificacionNotFoundException;
import com.duoc.servicio_calificaciones.model.Calificacion;
import com.duoc.servicio_calificaciones.repository.CalificacionesRepository;
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
class CalificacionesServiceTest {

    @Mock
    private CalificacionesRepository calificacionesRepository;

    @InjectMocks
    private CalificacionesService calificacionesService;

    @Test
    void guardar_deberiaCalcularPromedioYEstado() {
        CalificacionRequest request = new CalificacionRequest(1, 2, 5.0, 6.0, 7.0, LocalDate.of(2026, 6, 3));

        Calificacion saved = new Calificacion();
        saved.setId(1);
        saved.setEstudianteId(1);
        saved.setCursoId(2);
        saved.setNota1(5.0);
        saved.setNota2(6.0);
        saved.setNota3(7.0);
        saved.setNotaFinal(6.0);
        saved.setEstado("Aprobado");
        saved.setFecha(request.getFecha());

        when(calificacionesRepository.save(any(Calificacion.class))).thenReturn(saved);

        CalificacionDTO result = calificacionesService.guardar(request);

        assertEquals(6.0, result.getNotaFinal());
        assertEquals("Aprobado", result.getEstado());
    }

    @Test
    void listar_deberiaRetornarTodasLasCalificaciones() {
        Calificacion calificacion = new Calificacion();
        calificacion.setId(1);
        when(calificacionesRepository.findAll()).thenReturn(List.of(calificacion));

        List<CalificacionDTO> result = calificacionesService.listar();

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());
    }

    @Test
    void buscarPorId_deberiaRetornarDTOCuandoExiste() {
        Calificacion calificacion = new Calificacion();
        calificacion.setId(1);
        when(calificacionesRepository.findById(1)).thenReturn(Optional.of(calificacion));

        CalificacionDTO result = calificacionesService.buscarPorId(1);

        assertEquals(1, result.getId());
    }

    @Test
    void buscarPorId_deberiaLanzarExcepcionCuandoNoExiste() {
        when(calificacionesRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(CalificacionNotFoundException.class, () -> calificacionesService.buscarPorId(1));
    }

    @Test
    void buscarPorEstudiante_deberiaRetornarSoloLasCalificacionesDelEstudiante() {
        Calificacion calificacion = new Calificacion();
        calificacion.setId(1);
        calificacion.setEstudianteId(7);
        when(calificacionesRepository.findCalificacionesByEstudianteId(7)).thenReturn(List.of(calificacion));

        List<CalificacionDTO> result = calificacionesService.buscarPorEstudiante(7);

        assertEquals(1, result.size());
        assertEquals(7, result.get(0).getEstudianteId());
    }

    @Test
    void buscarPorCurso_deberiaRetornarSoloLasCalificacionesDelCurso() {
        Calificacion calificacion = new Calificacion();
        calificacion.setId(1);
        calificacion.setCursoId(3);
        when(calificacionesRepository.findCalificacionesByCursoId(3)).thenReturn(List.of(calificacion));

        List<CalificacionDTO> result = calificacionesService.buscarPorCurso(3);

        assertEquals(1, result.size());
        assertEquals(3, result.get(0).getCursoId());
    }

    @Test
    void actualizar_deberiaActualizarYRetornarDTO() {
        Calificacion existente = new Calificacion();
        existente.setId(1);
        existente.setEstudianteId(1);
        existente.setCursoId(2);

        CalificacionRequest request = new CalificacionRequest(9, 8, 4.0, 4.0, 4.0, LocalDate.of(2026, 6, 4));

        when(calificacionesRepository.findById(1)).thenReturn(Optional.of(existente));
        when(calificacionesRepository.save(existente)).thenReturn(existente);

        CalificacionDTO result = calificacionesService.actualizar(1, request);

        assertEquals(4.0, result.getNotaFinal());
        verify(calificacionesRepository).save(existente);
    }

    @Test
    void eliminar_deberiaBorrarSiExiste() {
        when(calificacionesRepository.existsById(1)).thenReturn(true);

        calificacionesService.eliminar(1);

        verify(calificacionesRepository).deleteById(1);
    }

    @Test
    void eliminar_deberiaLanzarExcepcionSiNoExiste() {
        when(calificacionesRepository.existsById(1)).thenReturn(false);

        assertThrows(CalificacionNotFoundException.class, () -> calificacionesService.eliminar(1));
        verify(calificacionesRepository, never()).deleteById(any());
    }
}