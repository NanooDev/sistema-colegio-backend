package com.duoc.servicio_profesores.service;

import com.duoc.servicio_profesores.dto.ProfesorDTO;
import com.duoc.servicio_profesores.dto.ProfesorRequest;
import com.duoc.servicio_profesores.exception.ProfesorNotFoundException;
import com.duoc.servicio_profesores.model.Profesor;
import com.duoc.servicio_profesores.repository.ProfesorRepository;
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
class ProfesorServiceTest {

    @Mock
    private ProfesorRepository profesorRepository;

    @InjectMocks
    private ProfesorService profesorService;

    // --- guardar ---

    @Test
    void guardar_conDatosValidos_deberiaCrearProfesor() {
        // Given
        ProfesorRequest request = new ProfesorRequest();
        request.setNombre("Juan");
        request.setApellido("Martinez");
        request.setEspecialidad("Matematicas");

        Profesor guardado = new Profesor();
        guardado.setId(1L);
        guardado.setNombre("Juan");
        guardado.setApellido("Martinez");
        guardado.setEspecialidad("Matematicas");

        when(profesorRepository.save(any(Profesor.class))).thenReturn(guardado);

        // When
        ProfesorDTO resultado = profesorService.guardar(request);

        // Then
        assertNotNull(resultado);
        assertEquals("Juan", resultado.getNombre());
        assertEquals("Matematicas", resultado.getEspecialidad());
        assertEquals("profesor", resultado.getCategoria());
        verify(profesorRepository).save(any(Profesor.class));
    }

    // --- listar ---

    @Test
    void listar_cuandoExistenProfesores_deberiaRetornarLista() {
        // Given
        Profesor prof = new Profesor();
        prof.setId(1L);
        prof.setNombre("Juan");
        prof.setApellido("Martinez");
        prof.setEspecialidad("Matematicas");

        when(profesorRepository.findAll()).thenReturn(List.of(prof));

        // When
        List<ProfesorDTO> resultado = profesorService.listar();

        // Then
        assertEquals(1, resultado.size());
        assertEquals("Juan", resultado.get(0).getNombre());
    }

    @Test
    void listar_cuandoNoExistenProfesores_deberiaRetornarListaVacia() {
        // Given
        when(profesorRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<ProfesorDTO> resultado = profesorService.listar();

        // Then
        assertTrue(resultado.isEmpty());
    }

    // --- buscarPorId ---

    @Test
    void buscarPorId_cuandoExiste_deberiaRetornarDTO() {
        // Given
        Profesor prof = new Profesor();
        prof.setId(5L);
        prof.setNombre("Laura");
        prof.setApellido("Soto");
        prof.setEspecialidad("Historia");

        when(profesorRepository.findById(5L)).thenReturn(Optional.of(prof));

        // When
        ProfesorDTO resultado = profesorService.buscarPorId(5L);

        // Then
        assertNotNull(resultado);
        assertEquals(5L, resultado.getId());
        assertEquals("Laura", resultado.getNombre());
    }

    @Test
    void buscarPorId_cuandoNoExiste_deberiaLanzarExcepcion() {
        // Given
        when(profesorRepository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(ProfesorNotFoundException.class, () -> profesorService.buscarPorId(99L));
    }

    // --- actualizar ---

    @Test
    void actualizar_cuandoExiste_deberiaActualizarYRetornarDTO() {
        // Given
        Profesor existente = new Profesor();
        existente.setId(3L);
        existente.setNombre("Carlos");
        existente.setApellido("Diaz");
        existente.setEspecialidad("Ciencias");

        ProfesorRequest request = new ProfesorRequest();
        request.setNombre("Carlos Andres");
        request.setApellido("Diaz");
        request.setEspecialidad("Fisica");

        Profesor actualizado = new Profesor();
        actualizado.setId(3L);
        actualizado.setNombre("Carlos Andres");
        actualizado.setApellido("Diaz");
        actualizado.setEspecialidad("Fisica");

        when(profesorRepository.findById(3L)).thenReturn(Optional.of(existente));
        when(profesorRepository.save(any(Profesor.class))).thenReturn(actualizado);

        // When
        ProfesorDTO resultado = profesorService.actualizar(3L, request);

        // Then
        assertEquals("Carlos Andres", resultado.getNombre());
        assertEquals("Fisica", resultado.getEspecialidad());
    }

    @Test
    void actualizar_cuandoNoExiste_deberiaLanzarExcepcion() {
        // Given
        ProfesorRequest request = new ProfesorRequest();
        request.setNombre("Test");
        request.setApellido("Test");
        request.setEspecialidad("Test");

        when(profesorRepository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(ProfesorNotFoundException.class, () -> profesorService.actualizar(99L, request));
        verify(profesorRepository, never()).save(any());
    }

    // --- eliminar ---

    @Test
    void eliminar_cuandoExiste_deberiaEliminar() {
        // Given
        Profesor prof = new Profesor();
        prof.setId(10L);

        when(profesorRepository.findById(10L)).thenReturn(Optional.of(prof));
        doNothing().when(profesorRepository).deleteById(10L);

        // When
        profesorService.eliminar(10L);

        // Then
        verify(profesorRepository).deleteById(10L);
    }

    @Test
    void eliminar_cuandoNoExiste_deberiaLanzarExcepcion() {
        // Given
        when(profesorRepository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(ProfesorNotFoundException.class, () -> profesorService.eliminar(99L));
        verify(profesorRepository, never()).deleteById(any());
    }
}
