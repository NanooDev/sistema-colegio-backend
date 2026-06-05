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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LibroServiceTest {

    @Mock
    private LibroRepository repository;

    @InjectMocks
    private LibroService libroService;

    @Test
    void guardar_deberiaCrearYRetornarDTO() {
        LibroRequest request = new LibroRequest();
        request.setTitulo("Clean Code");
        request.setAutor("Robert C. Martin");
        request.setEjemplaresDisponibles(3);

        Libro saved = new Libro();
        saved.setId(1L);
        saved.setTitulo("Clean Code");
        saved.setAutor("Robert C. Martin");
        saved.setEjemplaresDisponibles(3);

        when(repository.save(any(Libro.class))).thenReturn(saved);

        LibroDTO result = libroService.guardar(request);

        assertEquals(1L, result.getId());
        assertEquals("libro", result.getCategoria());
    }

    @Test
    void listar_deberiaRetornarTodosLosLibros() {
        Libro libro = new Libro();
        libro.setId(1L);
        libro.setTitulo("Clean Code");
        when(repository.findAll()).thenReturn(List.of(libro));

        List<LibroDTO> result = libroService.listar();

        assertEquals(1, result.size());
        assertEquals("Clean Code", result.get(0).getTitulo());
    }

    @Test
    void buscarPorId_deberiaRetornarDTOCuandoExiste() {
        Libro libro = new Libro();
        libro.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(libro));

        LibroDTO result = libroService.buscarPorId(1L);

        assertEquals(1L, result.getId());
    }

    @Test
    void buscarPorId_deberiaLanzarExcepcionCuandoNoExiste() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(LibroNotFoundException.class, () -> libroService.buscarPorId(1L));
    }

    @Test
    void actualizar_deberiaActualizarYRetornarDTO() {
        Libro existente = new Libro();
        existente.setId(1L);
        existente.setTitulo("Old");
        LibroRequest request = new LibroRequest();
        request.setTitulo("New");
        request.setAutor("Autor");
        request.setEjemplaresDisponibles(10);

        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(existente)).thenReturn(existente);

        LibroDTO result = libroService.actualizar(1L, request);

        assertEquals("New", result.getTitulo());
        verify(repository).save(existente);
    }

    @Test
    void eliminar_deberiaBorrarSiExiste() {
        Libro existente = new Libro();
        existente.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(existente));

        libroService.eliminar(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void eliminar_deberiaLanzarExcepcionSiNoExiste() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(LibroNotFoundException.class, () -> libroService.eliminar(1L));
        verify(repository, never()).deleteById(any());
    }
}