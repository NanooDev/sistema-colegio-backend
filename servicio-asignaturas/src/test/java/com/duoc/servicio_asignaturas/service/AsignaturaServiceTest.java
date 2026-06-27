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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AsignaturaServiceTest {

    @Mock
    private AsignaturaRepository repository;

    @InjectMocks
    private AsignaturaService asignaturaService;

    // --- guardar ---

    @Test
    void guardar_conDatosValidos_deberiaCrearAsignatura() {
        // Given
        AsignaturaRequest request = new AsignaturaRequest();
        request.setNombre("Matematicas");
        request.setCodigo("MAT101");

        Asignatura guardada = new Asignatura();
        guardada.setId(1L);
        guardada.setNombre("Matematicas");
        guardada.setCodigo("MAT101");

        when(repository.save(any(Asignatura.class))).thenReturn(guardada);

        // When
        AsignaturaDTO resultado = asignaturaService.guardar(request);

        // Then
        assertNotNull(resultado);
        assertEquals("Matematicas", resultado.getNombre());
        assertEquals("MAT101", resultado.getCodigo());
        assertEquals("asignatura", resultado.getCategoria());
        verify(repository).save(any(Asignatura.class));
    }

    // --- listar ---

    @Test
    void listar_cuandoExistenAsignaturas_deberiaRetornarLista() {
        // Given
        Asignatura asig = new Asignatura();
        asig.setId(1L);
        asig.setNombre("Matematicas");
        asig.setCodigo("MAT101");

        when(repository.findAll()).thenReturn(List.of(asig));

        // When
        List<AsignaturaDTO> resultado = asignaturaService.listar();

        // Then
        assertEquals(1, resultado.size());
        assertEquals("Matematicas", resultado.get(0).getNombre());
    }

    @Test
    void listar_cuandoNoExistenAsignaturas_deberiaRetornarListaVacia() {
        // Given
        when(repository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<AsignaturaDTO> resultado = asignaturaService.listar();

        // Then
        assertTrue(resultado.isEmpty());
    }

    // --- buscarPorId ---

    @Test
    void buscarPorId_cuandoExiste_deberiaRetornarDTO() {
        // Given
        Asignatura asig = new Asignatura();
        asig.setId(5L);
        asig.setNombre("Historia");
        asig.setCodigo("HIS201");

        when(repository.findById(5L)).thenReturn(Optional.of(asig));

        // When
        AsignaturaDTO resultado = asignaturaService.buscarPorId(5L);

        // Then
        assertNotNull(resultado);
        assertEquals(5L, resultado.getId());
        assertEquals("Historia", resultado.getNombre());
    }

    @Test
    void buscarPorId_cuandoNoExiste_deberiaLanzarExcepcion() {
        // Given
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(AsignaturaNotFoundException.class, () -> asignaturaService.buscarPorId(99L));
    }

    // --- actualizar ---

    @Test
    void actualizar_cuandoExiste_deberiaActualizarYRetornarDTO() {
        // Given
        Asignatura existente = new Asignatura();
        existente.setId(1L);
        existente.setNombre("Matematicas");
        existente.setCodigo("MAT101");

        AsignaturaRequest request = new AsignaturaRequest();
        request.setNombre("Algebra");
        request.setCodigo("ALG102");

        Asignatura actualizada = new Asignatura();
        actualizada.setId(1L);
        actualizada.setNombre("Algebra");
        actualizada.setCodigo("ALG102");

        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(any(Asignatura.class))).thenReturn(actualizada);

        // When
        AsignaturaDTO resultado = asignaturaService.actualizar(1L, request);

        // Then
        assertEquals("Algebra", resultado.getNombre());
        assertEquals("ALG102", resultado.getCodigo());
    }

    @Test
    void actualizar_cuandoNoExiste_deberiaLanzarExcepcion() {
        // Given
        AsignaturaRequest request = new AsignaturaRequest();
        request.setNombre("Test");
        request.setCodigo("TST");

        when(repository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(AsignaturaNotFoundException.class, () -> asignaturaService.actualizar(99L, request));
        verify(repository, never()).save(any());
    }

    // --- eliminar ---

    @Test
    void eliminar_cuandoExiste_deberiaEliminar() {
        // Given
        Asignatura asig = new Asignatura();
        asig.setId(10L);

        when(repository.findById(10L)).thenReturn(Optional.of(asig));
        doNothing().when(repository).deleteById(10L);

        // When
        asignaturaService.eliminar(10L);

        // Then
        verify(repository).deleteById(10L);
    }

    @Test
    void eliminar_cuandoNoExiste_deberiaLanzarExcepcion() {
        // Given
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(AsignaturaNotFoundException.class, () -> asignaturaService.eliminar(99L));
        verify(repository, never()).deleteById(any());
    }
}
