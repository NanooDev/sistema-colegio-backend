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

    @Test
    void guardar_deberiaLanzarExcepcion_siRutExiste() {
        EstudianteRequest request = new EstudianteRequest();
        request.setRut("20123456-7");

        when(estudianteRepository.existsByRut("20123456-7")).thenReturn(true);

        assertThrows(EstudianteDuplicadoException.class, () -> estudianteService.guardar(request));
        verify(estudianteRepository, never()).save(any(Estudiante.class));
    }

    @Test
    void guardar_deberiaCrearYRetornarDTO_siRutNoExiste() {
        EstudianteRequest request = new EstudianteRequest();
        request.setRut("20123456-7");
        request.setNombre("Ana");
        request.setApellido("Pérez");
        request.setCursoId(1L);

        Estudiante saved = new Estudiante();
        saved.setId(10);
        saved.setRut(request.getRut());
        saved.setNombre(request.getNombre());
        saved.setApellido(request.getApellido());
        saved.setCursoId(request.getCursoId());

        when(estudianteRepository.existsByRut(request.getRut())).thenReturn(false);
        when(estudianteRepository.save(any(Estudiante.class))).thenReturn(saved);

        EstudianteDTO result = estudianteService.guardar(request);

        assertNotNull(result);
        assertEquals(10, result.getId());
        assertEquals("Ana", result.getNombre());
        assertEquals("Pérez", result.getApellido());
        assertEquals("20123456-7", result.getRut());
        assertEquals(1L, result.getCursoId());
        assertEquals("estudiante", result.getCategoria());
    }

    @Test
    void listar_deberiaRetornarTodosLosEstudiantes() {
        Estudiante primero = new Estudiante();
        primero.setId(1);
        primero.setRut("11111111-1");
        primero.setNombre("Ana");
        primero.setApellido("Pérez");
        primero.setCursoId(10L);

        Estudiante segundo = new Estudiante();
        segundo.setId(2);
        segundo.setRut("22222222-2");
        segundo.setNombre("Luis");
        segundo.setApellido("González");
        segundo.setCursoId(11L);

        when(estudianteRepository.findAll()).thenReturn(List.of(primero, segundo));

        List<EstudianteDTO> result = estudianteService.listar();

        assertEquals(2, result.size());
        assertEquals("Ana", result.get(0).getNombre());
        assertEquals("Luis", result.get(1).getNombre());
    }

    @Test
    void buscarPorId_deberiaLanzarExcepcion_siNoExiste() {
        when(estudianteRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(EstudianteNotFoundException.class, () -> estudianteService.buscarPorId(99));
    }

    @Test
    void buscarPorId_deberiaRetornarDTO_siExiste() {
        Estudiante e = new Estudiante();
        e.setId(7);
        e.setNombre("Luis");
        e.setApellido("González");
        e.setRut("21111222-3");
        e.setCursoId(2L);

        when(estudianteRepository.findById(7)).thenReturn(Optional.of(e));

        EstudianteDTO result = estudianteService.buscarPorId(7);

        assertEquals(7, result.getId());
        assertEquals("Luis", result.getNombre());
        assertEquals("González", result.getApellido());
        assertEquals(2L, result.getCursoId());
    }

    @Test
    void actualizar_deberiaActualizarYRetornarDTO_siExiste() {
        Estudiante existente = new Estudiante();
        existente.setId(3);
        existente.setRut("33333333-3");
        existente.setNombre("Maria");
        existente.setApellido("Lopez");
        existente.setCursoId(4L);

        EstudianteRequest request = new EstudianteRequest();
        request.setRut("44444444-4");
        request.setNombre("María");
        request.setApellido("López");
        request.setCursoId(8L);

        Estudiante actualizado = new Estudiante();
        actualizado.setId(3);
        actualizado.setRut(request.getRut());
        actualizado.setNombre(request.getNombre());
        actualizado.setApellido(request.getApellido());
        actualizado.setCursoId(request.getCursoId());

        when(estudianteRepository.findById(3)).thenReturn(Optional.of(existente));
        when(estudianteRepository.save(any(Estudiante.class))).thenReturn(actualizado);

        EstudianteDTO result = estudianteService.actualizar(3, request);

        assertEquals(3, result.getId());
        assertEquals("44444444-4", result.getRut());
        assertEquals("María", result.getNombre());
        assertEquals("López", result.getApellido());
        assertEquals(8L, result.getCursoId());
        verify(estudianteRepository).save(existente);
    }

    @Test
    void eliminar_deberiaBorrarSiExiste() {
        Estudiante existente = new Estudiante();
        existente.setId(12);

        when(estudianteRepository.findById(12)).thenReturn(Optional.of(existente));

        estudianteService.eliminar(12);

        verify(estudianteRepository).deleteById(12);
    }

    @Test
    void eliminar_deberiaLanzarExcepcionSiNoExiste() {
        when(estudianteRepository.findById(12)).thenReturn(Optional.empty());

        assertThrows(EstudianteNotFoundException.class, () -> estudianteService.eliminar(12));
        verify(estudianteRepository, never()).deleteById(anyInt());
    }

    @Test
    void listarPorCurso_deberiaRetornarSoloCursoSolicitado() {
        Estudiante e1 = new Estudiante();
        e1.setId(1);
        e1.setNombre("A");
        e1.setApellido("B");
        e1.setRut("11111111-1");
        e1.setCursoId(4L);

        Estudiante e2 = new Estudiante();
        e2.setId(2);
        e2.setNombre("C");
        e2.setApellido("D");
        e2.setRut("22222222-2");
        e2.setCursoId(4L);

        when(estudianteRepository.findByCursoId(4L)).thenReturn(List.of(e1, e2));

        List<EstudianteDTO> result = estudianteService.listarPorCurso(4L);

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(r -> r.getCursoId().equals(4L)));
    }
}
