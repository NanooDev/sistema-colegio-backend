package com.duoc.servicio_calificaciones.service;

import com.duoc.servicio_calificaciones.client.EstudianteFeign;
import com.duoc.servicio_calificaciones.dto.CalificacionDTO;
import com.duoc.servicio_calificaciones.dto.CalificacionRequest;
import com.duoc.servicio_calificaciones.dto.EstudianteDTO;
import com.duoc.servicio_calificaciones.exception.CalificacionNotFoundException;
import com.duoc.servicio_calificaciones.model.Calificacion;
import com.duoc.servicio_calificaciones.repository.CalificacionesRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CalificacionesServiceTest {

    @Mock
    private CalificacionesRepository calificacionesRepository;

    @Mock
    private EstudianteFeign estudianteFeign;

    @InjectMocks
    private CalificacionesService calificacionesService;

    // --- guardar ---

    @Test
    void guardar_conNotasAprobatorias_deberiaCalcularPromedioYEstadoAprobado() {
        // Given
        CalificacionRequest request = new CalificacionRequest();
        request.setEstudianteId(1);
        request.setCursoId(2);
        request.setNota1(6.0);
        request.setNota2(5.0);
        request.setNota3(7.0);
        request.setFecha(LocalDate.of(2025, 6, 15));

        Calificacion guardada = new Calificacion();
        guardada.setId(1);
        guardada.setEstudianteId(1);
        guardada.setCursoId(2);
        guardada.setNota1(6.0);
        guardada.setNota2(5.0);
        guardada.setNota3(7.0);
        guardada.setNotaFinal(6.0);
        guardada.setEstado("Aprobado");
        guardada.setFecha(LocalDate.of(2025, 6, 15));

        when(calificacionesRepository.save(any(Calificacion.class))).thenReturn(guardada);

        // When
        CalificacionDTO resultado = calificacionesService.guardar(request);

        // Then
        assertNotNull(resultado);
        assertEquals(6.0, resultado.getNotaFinal());
        assertEquals("Aprobado", resultado.getEstado());
        assertEquals("calificacion", resultado.getCategoria());
        verify(calificacionesRepository).save(any(Calificacion.class));
    }

    @Test
    void guardar_conNotasReprobatorias_deberiaCalcularEstadoReprobado() {
        // Given
        CalificacionRequest request = new CalificacionRequest();
        request.setEstudianteId(2);
        request.setCursoId(1);
        request.setNota1(2.0);
        request.setNota2(3.0);
        request.setNota3(3.5);
        request.setFecha(LocalDate.of(2025, 6, 15));

        Calificacion guardada = new Calificacion();
        guardada.setId(2);
        guardada.setEstudianteId(2);
        guardada.setCursoId(1);
        guardada.setNota1(2.0);
        guardada.setNota2(3.0);
        guardada.setNota3(3.5);
        guardada.setNotaFinal(2.833333333333333);
        guardada.setEstado("Reprobado");
        guardada.setFecha(LocalDate.of(2025, 6, 15));

        when(calificacionesRepository.save(any(Calificacion.class))).thenReturn(guardada);

        // When
        CalificacionDTO resultado = calificacionesService.guardar(request);

        // Then
        assertNotNull(resultado);
        assertEquals("Reprobado", resultado.getEstado());
        assertTrue(resultado.getNotaFinal() < 4.0);
    }

    @Test
    void guardar_conNotaLimite4_deberiaSerAprobado() {
        // Given
        CalificacionRequest request = new CalificacionRequest();
        request.setEstudianteId(3);
        request.setCursoId(1);
        request.setNota1(4.0);
        request.setNota2(4.0);
        request.setNota3(4.0);
        request.setFecha(LocalDate.of(2025, 6, 15));

        Calificacion guardada = new Calificacion();
        guardada.setId(3);
        guardada.setEstudianteId(3);
        guardada.setCursoId(1);
        guardada.setNota1(4.0);
        guardada.setNota2(4.0);
        guardada.setNota3(4.0);
        guardada.setNotaFinal(4.0);
        guardada.setEstado("Aprobado");
        guardada.setFecha(LocalDate.of(2025, 6, 15));

        when(calificacionesRepository.save(any(Calificacion.class))).thenReturn(guardada);

        // When
        CalificacionDTO resultado = calificacionesService.guardar(request);

        // Then
        assertEquals(4.0, resultado.getNotaFinal());
        assertEquals("Aprobado", resultado.getEstado());
    }

    // --- listar ---

    @Test
    void listar_cuandoExistenCalificaciones_deberiaRetornarLista() {
        // Given
        Calificacion cal = new Calificacion();
        cal.setId(1);
        cal.setEstudianteId(1);
        cal.setCursoId(2);
        cal.setNota1(5.0);
        cal.setNota2(6.0);
        cal.setNota3(7.0);
        cal.setNotaFinal(6.0);
        cal.setEstado("Aprobado");
        cal.setFecha(LocalDate.now());

        when(calificacionesRepository.findAll()).thenReturn(List.of(cal));

        // When
        List<CalificacionDTO> resultado = calificacionesService.listar();

        // Then
        assertEquals(1, resultado.size());
        assertEquals("Aprobado", resultado.get(0).getEstado());
    }

    @Test
    void listar_cuandoNoExistenCalificaciones_deberiaRetornarListaVacia() {
        // Given
        when(calificacionesRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<CalificacionDTO> resultado = calificacionesService.listar();

        // Then
        assertTrue(resultado.isEmpty());
    }

    // --- buscarPorId ---

    @Test
    void buscarPorId_cuandoExiste_deberiaRetornarDTO() {
        // Given
        Calificacion cal = new Calificacion();
        cal.setId(5);
        cal.setEstudianteId(2);
        cal.setCursoId(3);
        cal.setNota1(6.5);
        cal.setNota2(5.5);
        cal.setNota3(6.0);
        cal.setNotaFinal(6.0);
        cal.setEstado("Aprobado");
        cal.setFecha(LocalDate.now());

        when(calificacionesRepository.findById(5)).thenReturn(Optional.of(cal));

        // When
        CalificacionDTO resultado = calificacionesService.buscarPorId(5);

        // Then
        assertNotNull(resultado);
        assertEquals(5, resultado.getId());
        assertEquals(6.0, resultado.getNotaFinal());
    }

    @Test
    void buscarPorId_cuandoNoExiste_deberiaLanzarExcepcion() {
        // Given
        when(calificacionesRepository.findById(99)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(CalificacionNotFoundException.class, () -> calificacionesService.buscarPorId(99));
    }

    // --- buscarPorEstudiante ---

    @Test
    void buscarPorEstudiante_cuandoExisten_deberiaRetornarLista() {
        // Given
        Calificacion cal = new Calificacion();
        cal.setId(1);
        cal.setEstudianteId(1);
        cal.setCursoId(2);
        cal.setNota1(5.0);
        cal.setNota2(6.0);
        cal.setNota3(7.0);
        cal.setNotaFinal(6.0);
        cal.setEstado("Aprobado");
        cal.setFecha(LocalDate.now());

        when(calificacionesRepository.findCalificacionesByEstudianteId(1)).thenReturn(List.of(cal));

        // When
        List<CalificacionDTO> resultado = calificacionesService.buscarPorEstudiante(1);

        // Then
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.get(0).getEstudianteId());
    }

    @Test
    void buscarPorEstudiante_cuandoNoExisten_deberiaRetornarListaVacia() {
        // Given
        when(calificacionesRepository.findCalificacionesByEstudianteId(99)).thenReturn(Collections.emptyList());

        // When
        List<CalificacionDTO> resultado = calificacionesService.buscarPorEstudiante(99);

        // Then
        assertTrue(resultado.isEmpty());
    }

    // --- buscarPorCurso ---

    @Test
    void buscarPorCurso_cuandoExisten_deberiaRetornarLista() {
        // Given
        Calificacion cal = new Calificacion();
        cal.setId(1);
        cal.setEstudianteId(1);
        cal.setCursoId(2);
        cal.setNota1(5.0);
        cal.setNota2(6.0);
        cal.setNota3(7.0);
        cal.setNotaFinal(6.0);
        cal.setEstado("Aprobado");
        cal.setFecha(LocalDate.now());

        when(calificacionesRepository.findCalificacionesByCursoId(2)).thenReturn(List.of(cal));

        // When
        List<CalificacionDTO> resultado = calificacionesService.buscarPorCurso(2);

        // Then
        assertFalse(resultado.isEmpty());
        assertEquals(2, resultado.get(0).getCursoId());
    }

    // --- actualizar ---

    @Test
    void actualizar_cuandoExiste_deberiaRecalcularPromedioYEstado() {
        // Given
        Calificacion existente = new Calificacion();
        existente.setId(1);
        existente.setEstudianteId(1);
        existente.setCursoId(2);
        existente.setNota1(3.0);
        existente.setNota2(3.0);
        existente.setNota3(3.0);
        existente.setNotaFinal(3.0);
        existente.setEstado("Reprobado");
        existente.setFecha(LocalDate.of(2025, 6, 1));

        CalificacionRequest request = new CalificacionRequest();
        request.setEstudianteId(1);
        request.setCursoId(2);
        request.setNota1(5.0);
        request.setNota2(6.0);
        request.setNota3(7.0);
        request.setFecha(LocalDate.of(2025, 6, 20));

        Calificacion actualizada = new Calificacion();
        actualizada.setId(1);
        actualizada.setEstudianteId(1);
        actualizada.setCursoId(2);
        actualizada.setNota1(5.0);
        actualizada.setNota2(6.0);
        actualizada.setNota3(7.0);
        actualizada.setNotaFinal(6.0);
        actualizada.setEstado("Aprobado");
        actualizada.setFecha(LocalDate.of(2025, 6, 20));

        when(calificacionesRepository.findById(1)).thenReturn(Optional.of(existente));
        when(calificacionesRepository.save(any(Calificacion.class))).thenReturn(actualizada);

        // When
        CalificacionDTO resultado = calificacionesService.actualizar(1, request);

        // Then
        assertEquals(6.0, resultado.getNotaFinal());
        assertEquals("Aprobado", resultado.getEstado());
    }

    @Test
    void actualizar_cuandoNoExiste_deberiaLanzarExcepcion() {
        // Given
        CalificacionRequest request = new CalificacionRequest();
        request.setEstudianteId(1);
        request.setCursoId(2);
        request.setNota1(5.0);
        request.setNota2(5.0);
        request.setNota3(5.0);
        request.setFecha(LocalDate.now());

        when(calificacionesRepository.findById(99)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(CalificacionNotFoundException.class, () -> calificacionesService.actualizar(99, request));
        verify(calificacionesRepository, never()).save(any());
    }

    // --- eliminar ---

    @Test
    void eliminar_cuandoExiste_deberiaEliminar() {
        // Given
        when(calificacionesRepository.existsById(10)).thenReturn(true);
        doNothing().when(calificacionesRepository).deleteById(10);

        // When
        calificacionesService.eliminar(10);

        // Then
        verify(calificacionesRepository).deleteById(10);
    }

    @Test
    void eliminar_cuandoNoExiste_deberiaLanzarExcepcion() {
        // Given
        when(calificacionesRepository.existsById(99)).thenReturn(false);

        // When / Then
        assertThrows(CalificacionNotFoundException.class, () -> calificacionesService.eliminar(99));
        verify(calificacionesRepository, never()).deleteById(any());
    }

    // --- obtenerCalificacionConEstudiante ---

    @Test
    void obtenerConEstudiante_cuandoExiste_deberiaEnriquecerConNombre() {
        // Given
        Calificacion cal = new Calificacion();
        cal.setId(1);
        cal.setEstudianteId(5);
        cal.setCursoId(2);
        cal.setNota1(6.0);
        cal.setNota2(5.0);
        cal.setNota3(7.0);
        cal.setNotaFinal(6.0);
        cal.setEstado("Aprobado");
        cal.setFecha(LocalDate.now());

        EstudianteDTO estDTO = new EstudianteDTO();
        estDTO.setId(5);
        estDTO.setNombre("Ana");
        estDTO.setApellido("Perez");

        when(calificacionesRepository.findById(1)).thenReturn(Optional.of(cal));
        when(estudianteFeign.obtenerEstudiantePorId(5)).thenReturn(estDTO);

        // When
        CalificacionDTO resultado = calificacionesService.obtenerCalificacionConEstudiante(1);

        // Then
        assertNotNull(resultado);
        assertEquals("Ana Perez", resultado.getNombreEstudiante());
        assertEquals("Aprobado", resultado.getEstado());
        verify(estudianteFeign).obtenerEstudiantePorId(5);
    }

    @Test
    void obtenerConEstudiante_cuandoFeignFalla_deberiaRetornarSinNombre() {
        // Given
        Calificacion cal = new Calificacion();
        cal.setId(1);
        cal.setEstudianteId(5);
        cal.setCursoId(2);
        cal.setNota1(6.0);
        cal.setNota2(5.0);
        cal.setNota3(7.0);
        cal.setNotaFinal(6.0);
        cal.setEstado("Aprobado");
        cal.setFecha(LocalDate.now());

        when(calificacionesRepository.findById(1)).thenReturn(Optional.of(cal));
        when(estudianteFeign.obtenerEstudiantePorId(5)).thenThrow(new RuntimeException("Servicio no disponible"));

        // When
        CalificacionDTO resultado = calificacionesService.obtenerCalificacionConEstudiante(1);

        // Then
        assertNotNull(resultado);
        assertNull(resultado.getNombreEstudiante());
    }
}
