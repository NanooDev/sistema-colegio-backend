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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProfesorServiceTest {

    @Mock
    private ProfesorRepository profesorRepository;

    @InjectMocks
    private ProfesorService profesorService;

    @Test
    void guardar_deberiaCrearYRetornarDTO() {
        ProfesorRequest request = new ProfesorRequest();
        request.setNombre("Juan");
        request.setApellido("Perez");
        request.setEspecialidad("Matematicas");

        Profesor saved = new Profesor();
        saved.setId(1L);
        saved.setNombre("Juan");
        saved.setApellido("Perez");
        saved.setEspecialidad("Matematicas");

        when(profesorRepository.save(any(Profesor.class))).thenReturn(saved);

        ProfesorDTO result = profesorService.guardar(request);

        assertEquals(1L, result.getId());
        assertEquals("profesor", result.getCategoria());
        assertEquals("Juan", result.getNombre());
    }

    @Test
    void listar_deberiaRetornarTodosLosProfesores() {
        Profesor primero = new Profesor();
        primero.setId(1L);
        primero.setNombre("Juan");
        Profesor segundo = new Profesor();
        segundo.setId(2L);
        segundo.setNombre("Maria");

        when(profesorRepository.findAll()).thenReturn(List.of(primero, segundo));

        List<ProfesorDTO> result = profesorService.listar();

        assertEquals(2, result.size());
        assertEquals("Juan", result.get(0).getNombre());
        assertEquals("Maria", result.get(1).getNombre());
    }

    @Test
    void buscarPorId_deberiaRetornarDTOCuandoExiste() {
        Profesor profesor = new Profesor();
        profesor.setId(5L);
        profesor.setNombre("Luis");

        when(profesorRepository.findById(5L)).thenReturn(Optional.of(profesor));

        ProfesorDTO result = profesorService.buscarPorId(5L);

        assertEquals(5L, result.getId());
        assertEquals("profesor", result.getCategoria());
    }

    @Test
    void buscarPorId_deberiaLanzarExcepcionCuandoNoExiste() {
        when(profesorRepository.findById(5L)).thenReturn(Optional.empty());

        assertThrows(ProfesorNotFoundException.class, () -> profesorService.buscarPorId(5L));
    }

    @Test
    void actualizar_deberiaActualizarYRetornarDTO() {
        Profesor existente = new Profesor();
        existente.setId(3L);
        existente.setNombre("Ana");
        existente.setApellido("Lopez");
        existente.setEspecialidad("Historia");

        ProfesorRequest request = new ProfesorRequest();
        request.setNombre("Ana Maria");
        request.setApellido("Lopez");
        request.setEspecialidad("Historia");

        when(profesorRepository.findById(3L)).thenReturn(Optional.of(existente));
        when(profesorRepository.save(existente)).thenReturn(existente);

        ProfesorDTO result = profesorService.actualizar(3L, request);

        assertEquals("Ana Maria", result.getNombre());
        verify(profesorRepository).save(existente);
    }

    @Test
    void eliminar_deberiaBorrarSiExiste() {
        Profesor existente = new Profesor();
        existente.setId(3L);
        when(profesorRepository.findById(3L)).thenReturn(Optional.of(existente));

        profesorService.eliminar(3L);

        verify(profesorRepository).deleteById(3L);
    }

    @Test
    void eliminar_deberiaLanzarExcepcionSiNoExiste() {
        when(profesorRepository.findById(3L)).thenReturn(Optional.empty());

        assertThrows(ProfesorNotFoundException.class, () -> profesorService.eliminar(3L));
        verify(profesorRepository, never()).deleteById(any());
    }
}