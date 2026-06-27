package com.duoc.servicio_biblioteca.service;

import com.duoc.servicio_biblioteca.dto.LibroDTO;
import com.duoc.servicio_biblioteca.dto.LibroRequest;
import com.duoc.servicio_biblioteca.exception.LibroNotFoundException;
import com.duoc.servicio_biblioteca.model.Libro;
import com.duoc.servicio_biblioteca.repository.LibroRepository;
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
class LibroServiceTest {

    @Mock
    private LibroRepository repository;

    @InjectMocks
    private LibroService libroService;

    // --- guardar ---

    @Test
    void guardar_conDatosValidos_deberiaCrearLibro() {
        // Given
        LibroRequest request = new LibroRequest();
        request.setTitulo("Don Quijote");
        request.setAutor("Cervantes");
        request.setEjemplaresDisponibles(5);

        Libro guardado = new Libro();
        guardado.setId(1L);
        guardado.setTitulo("Don Quijote");
        guardado.setAutor("Cervantes");
        guardado.setEjemplaresDisponibles(5);

        when(repository.save(any(Libro.class))).thenReturn(guardado);

        // When
        LibroDTO resultado = libroService.guardar(request);

        // Then
        assertNotNull(resultado);
        assertEquals("Don Quijote", resultado.getTitulo());
        assertEquals("Cervantes", resultado.getAutor());
        assertEquals(5, resultado.getEjemplaresDisponibles());
        assertEquals("libro", resultado.getCategoria());
        verify(repository).save(any(Libro.class));
    }

    // --- listar ---

    @Test
    void listar_cuandoExistenLibros_deberiaRetornarLista() {
        // Given
        Libro libro = new Libro();
        libro.setId(1L);
        libro.setTitulo("Don Quijote");
        libro.setAutor("Cervantes");
        libro.setEjemplaresDisponibles(5);

        when(repository.findAll()).thenReturn(List.of(libro));

        // When
        List<LibroDTO> resultado = libroService.listar();

        // Then
        assertEquals(1, resultado.size());
        assertEquals("Don Quijote", resultado.get(0).getTitulo());
    }

    @Test
    void listar_cuandoNoExistenLibros_deberiaRetornarListaVacia() {
        // Given
        when(repository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<LibroDTO> resultado = libroService.listar();

        // Then
        assertTrue(resultado.isEmpty());
    }

    // --- buscarPorId ---

    @Test
    void buscarPorId_cuandoExiste_deberiaRetornarDTO() {
        // Given
        Libro libro = new Libro();
        libro.setId(5L);
        libro.setTitulo("Cien Anios de Soledad");
        libro.setAutor("Garcia Marquez");
        libro.setEjemplaresDisponibles(3);

        when(repository.findById(5L)).thenReturn(Optional.of(libro));

        // When
        LibroDTO resultado = libroService.buscarPorId(5L);

        // Then
        assertNotNull(resultado);
        assertEquals(5L, resultado.getId());
        assertEquals("Cien Anios de Soledad", resultado.getTitulo());
    }

    @Test
    void buscarPorId_cuandoNoExiste_deberiaLanzarExcepcion() {
        // Given
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(LibroNotFoundException.class, () -> libroService.buscarPorId(99L));
    }

    // --- actualizar ---

    @Test
    void actualizar_cuandoExiste_deberiaActualizarYRetornarDTO() {
        // Given
        Libro existente = new Libro();
        existente.setId(1L);
        existente.setTitulo("Don Quijote");
        existente.setAutor("Cervantes");
        existente.setEjemplaresDisponibles(5);

        LibroRequest request = new LibroRequest();
        request.setTitulo("Don Quijote de la Mancha");
        request.setAutor("Miguel de Cervantes");
        request.setEjemplaresDisponibles(10);

        Libro actualizado = new Libro();
        actualizado.setId(1L);
        actualizado.setTitulo("Don Quijote de la Mancha");
        actualizado.setAutor("Miguel de Cervantes");
        actualizado.setEjemplaresDisponibles(10);

        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(any(Libro.class))).thenReturn(actualizado);

        // When
        LibroDTO resultado = libroService.actualizar(1L, request);

        // Then
        assertEquals("Don Quijote de la Mancha", resultado.getTitulo());
        assertEquals(10, resultado.getEjemplaresDisponibles());
    }

    @Test
    void actualizar_cuandoNoExiste_deberiaLanzarExcepcion() {
        // Given
        LibroRequest request = new LibroRequest();
        request.setTitulo("Test");
        request.setAutor("Test");
        request.setEjemplaresDisponibles(1);

        when(repository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(LibroNotFoundException.class, () -> libroService.actualizar(99L, request));
        verify(repository, never()).save(any());
    }

    // --- eliminar ---

    @Test
    void eliminar_cuandoExiste_deberiaEliminar() {
        // Given
        Libro libro = new Libro();
        libro.setId(10L);

        when(repository.findById(10L)).thenReturn(Optional.of(libro));
        doNothing().when(repository).deleteById(10L);

        // When
        libroService.eliminar(10L);

        // Then
        verify(repository).deleteById(10L);
    }

    @Test
    void eliminar_cuandoNoExiste_deberiaLanzarExcepcion() {
        // Given
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(LibroNotFoundException.class, () -> libroService.eliminar(99L));
        verify(repository, never()).deleteById(any());
    }
}
