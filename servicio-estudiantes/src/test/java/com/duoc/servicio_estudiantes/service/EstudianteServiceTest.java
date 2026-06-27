package com.duoc.servicio_estudiantes.service;

import com.duoc.servicio_estudiantes.dto.EstudianteDTO;
import com.duoc.servicio_estudiantes.dto.EstudianteRequest;
import com.duoc.servicio_estudiantes.exception.EstudianteDuplicadoException;
import com.duoc.servicio_estudiantes.exception.EstudianteNotFoundException;
import com.duoc.servicio_estudiantes.model.Estudiante;
import com.duoc.servicio_estudiantes.repository.EstudianteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EstudianteServiceTest {

    @Mock
    private EstudianteRepository estudianteRepository;

    @InjectMocks
    private EstudianteService estudianteService;

    // --- guardar ---

    @Test
    void guardar_cuandoRutNoExiste_deberiaCrearEstudiante() {
        // Given
        EstudianteRequest request = new EstudianteRequest();
        request.setRut("20123456-7");
        request.setNombre("Ana");
        request.setApellido("Perez");
        request.setCursoId(1L);

        Estudiante guardado = new Estudiante();
        guardado.setId(1);
        guardado.setRut("20123456-7");
        guardado.setNombre("Ana");
        guardado.setApellido("Perez");
        guardado.setCursoId(1L);

        when(estudianteRepository.existsByRut("20123456-7")).thenReturn(false);
        when(estudianteRepository.save(any(Estudiante.class))).thenReturn(guardado);

        // When
        EstudianteDTO resultado = estudianteService.guardar(request);

        // Then
        assertNotNull(resultado);
        assertEquals("Ana", resultado.getNombre());
        assertEquals("20123456-7", resultado.getRut());
        assertEquals(1L, resultado.getCursoId());
        verify(estudianteRepository).existsByRut("20123456-7");
        verify(estudianteRepository).save(any(Estudiante.class));
    }

    @Test
    void guardar_cuandoRutYaExiste_deberiaLanzarExcepcion() {
        // Given
        EstudianteRequest request = new EstudianteRequest();
        request.setRut("20123456-7");
        request.setNombre("Ana");
        request.setApellido("Perez");
        request.setCursoId(1L);

        when(estudianteRepository.existsByRut("20123456-7")).thenReturn(true);

        // When / Then
        assertThrows(EstudianteDuplicadoException.class, () -> estudianteService.guardar(request));
        verify(estudianteRepository).existsByRut("20123456-7");
        verify(estudianteRepository, never()).save(any());
    }

    // --- listar ---

    @Test
    void listar_cuandoExistenEstudiantes_deberiaRetornarLista() {
        // Given
        Estudiante est = new Estudiante();
        est.setId(1);
        est.setRut("20123456-7");
        est.setNombre("Ana");
        est.setApellido("Perez");
        est.setCursoId(1L);

        when(estudianteRepository.findAll()).thenReturn(List.of(est));

        // When
        List<EstudianteDTO> resultado = estudianteService.listar();

        // Then
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals("Ana", resultado.get(0).getNombre());
    }

    @Test
    void listar_cuandoNoExistenEstudiantes_deberiaRetornarListaVacia() {
        // Given
        when(estudianteRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<EstudianteDTO> resultado = estudianteService.listar();

        // Then
        assertTrue(resultado.isEmpty());
    }

    // --- buscarPorId ---

    @Test
    void buscarPorId_cuandoExiste_deberiaRetornarDTO() {
        // Given
        Estudiante est = new Estudiante();
        est.setId(5);
        est.setRut("11111111-1");
        est.setNombre("Luis");
        est.setApellido("Gonzalez");
        est.setCursoId(2L);

        when(estudianteRepository.findById(5)).thenReturn(Optional.of(est));

        // When
        EstudianteDTO resultado = estudianteService.buscarPorId(5);

        // Then
        assertNotNull(resultado);
        assertEquals(5, resultado.getId());
        assertEquals("Luis", resultado.getNombre());
    }

    @Test
    void buscarPorId_cuandoNoExiste_deberiaLanzarExcepcion() {
        // Given
        when(estudianteRepository.findById(99)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(EstudianteNotFoundException.class, () -> estudianteService.buscarPorId(99));
    }

    // --- actualizar ---

    @Test
    void actualizar_cuandoExiste_deberiaActualizarYRetornarDTO() {
        // Given
        Estudiante existente = new Estudiante();
        existente.setId(3);
        existente.setRut("33333333-3");
        existente.setNombre("Carlos");
        existente.setApellido("Diaz");
        existente.setCursoId(1L);

        EstudianteRequest request = new EstudianteRequest();
        request.setRut("44444444-4");
        request.setNombre("Maria");
        request.setApellido("Lopez");
        request.setCursoId(2L);

        Estudiante actualizado = new Estudiante();
        actualizado.setId(3);
        actualizado.setRut("44444444-4");
        actualizado.setNombre("Maria");
        actualizado.setApellido("Lopez");
        actualizado.setCursoId(2L);

        when(estudianteRepository.findById(3)).thenReturn(Optional.of(existente));
        when(estudianteRepository.save(any(Estudiante.class))).thenReturn(actualizado);

        // When
        EstudianteDTO resultado = estudianteService.actualizar(3, request);

        // Then
        assertNotNull(resultado);
        assertEquals("Maria", resultado.getNombre());
        assertEquals("44444444-4", resultado.getRut());
        assertEquals(2L, resultado.getCursoId());
    }

    @Test
    void actualizar_cuandoNoExiste_deberiaLanzarExcepcion() {
        // Given
        EstudianteRequest request = new EstudianteRequest();
        request.setRut("44444444-4");
        request.setNombre("Maria");
        request.setApellido("Lopez");
        request.setCursoId(2L);

        when(estudianteRepository.findById(99)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(EstudianteNotFoundException.class, () -> estudianteService.actualizar(99, request));
        verify(estudianteRepository, never()).save(any());
    }

    // --- eliminar ---

    @Test
    void eliminar_cuandoExiste_deberiaEliminar() {
        // Given
        Estudiante est = new Estudiante();
        est.setId(10);

        when(estudianteRepository.findById(10)).thenReturn(Optional.of(est));
        doNothing().when(estudianteRepository).deleteById(10);

        // When
        estudianteService.eliminar(10);

        // Then
        verify(estudianteRepository).deleteById(10);
    }

    @Test
    void eliminar_cuandoNoExiste_deberiaLanzarExcepcion() {
        // Given
        when(estudianteRepository.findById(99)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(EstudianteNotFoundException.class, () -> estudianteService.eliminar(99));
        verify(estudianteRepository, never()).deleteById(any());
    }

    // --- listarPorCurso ---

    @Test
    void listarPorCurso_cuandoExisten_deberiaRetornarLista() {
        // Given
        Estudiante est = new Estudiante();
        est.setId(1);
        est.setRut("20123456-7");
        est.setNombre("Ana");
        est.setApellido("Perez");
        est.setCursoId(4L);

        when(estudianteRepository.findByCursoId(4L)).thenReturn(List.of(est));

        // When
        List<EstudianteDTO> resultado = estudianteService.listarPorCurso(4L);

        // Then
        assertFalse(resultado.isEmpty());
        assertEquals(4L, resultado.get(0).getCursoId());
    }

    @Test
    void listarPorCurso_cuandoNoExisten_deberiaRetornarListaVacia() {
        // Given
        when(estudianteRepository.findByCursoId(99L)).thenReturn(Collections.emptyList());

        // When
        List<EstudianteDTO> resultado = estudianteService.listarPorCurso(99L);

        // Then
        assertTrue(resultado.isEmpty());
    }
}
