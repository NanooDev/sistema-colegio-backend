package com.duoc.servicio_cursos.service;

import com.duoc.servicio_cursos.client.EstudianteFeign;
import com.duoc.servicio_cursos.client.ProfesorFeign;
import com.duoc.servicio_cursos.dto.CursoCreateRequest;
import com.duoc.servicio_cursos.dto.EstudianteDTO;
import com.duoc.servicio_cursos.dto.ProfesorDTO;
import com.duoc.servicio_cursos.entity.CursoEntity;
import com.duoc.servicio_cursos.exception.CursoNotFoundException;
import com.duoc.servicio_cursos.model.Curso;
import com.duoc.servicio_cursos.repository.CursoEntityRepository;
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
class CursoServiceImplTest {

    @Mock
    private CursoEntityRepository cursoEntityRepository;

    @Mock
    private ProfesorFeign profesorFeign;

    @Mock
    private EstudianteFeign estudianteFeign;

    @InjectMocks
    private CursoServiceImpl cursoService;

    @Test
    void listarCursos_deberiaRetornarListaDeCursos() {
        CursoEntity entity = new CursoEntity();
        entity.setId(1L);
        entity.setNombre("1A");
        entity.setProfesorJefeId(10L);

        when(cursoEntityRepository.findAll()).thenReturn(List.of(entity));

        List<Curso> result = cursoService.listarCursos();

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals("1A", result.get(0).getNombre());
    }

    @Test
    void crear_deberiaGuardarYRetornarCurso() {
        CursoCreateRequest request = new CursoCreateRequest();
        request.setNombre("1A");
        request.setProfesorJefeId(10L);

        CursoEntity saved = new CursoEntity();
        saved.setId(1L);
        saved.setNombre("1A");
        saved.setProfesorJefeId(10L);

        when(cursoEntityRepository.save(any(CursoEntity.class))).thenReturn(saved);

        Curso result = cursoService.crear(request);

        assertEquals(1L, result.getId());
        assertEquals("1A", result.getNombre());
    }

    @Test
    void actualizar_deberiaActualizarYRetornarCurso() {
        CursoEntity existing = new CursoEntity();
        existing.setId(1L);
        existing.setNombre("Old");
        existing.setProfesorJefeId(3L);

        CursoCreateRequest request = new CursoCreateRequest();
        request.setNombre("New");
        request.setProfesorJefeId(5L);

        when(cursoEntityRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(cursoEntityRepository.save(existing)).thenReturn(existing);

        Curso result = cursoService.actualizar(1L, request);

        assertEquals("New", result.getNombre());
        assertEquals(5L, result.getProfesorJefeId());
        verify(cursoEntityRepository).save(existing);
    }

    @Test
    void actualizar_deberiaLanzarExcepcionSiNoExiste() {
        CursoCreateRequest request = new CursoCreateRequest();
        request.setNombre("New");
        request.setProfesorJefeId(5L);
        when(cursoEntityRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CursoNotFoundException.class, () -> cursoService.actualizar(1L, request));
    }

    @Test
    void eliminar_deberiaBorrarEntidadSiExiste() {
        CursoEntity existing = new CursoEntity();
        existing.setId(1L);
        when(cursoEntityRepository.findById(1L)).thenReturn(Optional.of(existing));

        cursoService.eliminar(1L);

        verify(cursoEntityRepository).delete(existing);
    }

    @Test
    void eliminar_deberiaLanzarExcepcionSiNoExiste() {
        when(cursoEntityRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CursoNotFoundException.class, () -> cursoService.eliminar(1L));
        verify(cursoEntityRepository, never()).delete(any());
    }

    @Test
    void obtenerCursoConDetalles_deberiaCompletarProfesorYEstudiantes() {
        CursoEntity entity = new CursoEntity();
        entity.setId(1L);
        entity.setNombre("1A");
        entity.setProfesorJefeId(10L);
        when(cursoEntityRepository.findById(1L)).thenReturn(Optional.of(entity));

        ProfesorDTO profesor = new ProfesorDTO();
        profesor.setId(10L);
        profesor.setNombre("Juan");
        when(profesorFeign.obtenerProfesorPorId(10L)).thenReturn(profesor);

        EstudianteDTO estudiante = new EstudianteDTO();
        estudiante.setId(2L);
        estudiante.setNombre("Ana");
        when(estudianteFeign.obtenerEstudiantesPorCurso(1L)).thenReturn(List.of(estudiante));

        Curso result = cursoService.obtenerCursoConDetalles(1L);

        assertEquals(10L, result.getProfesorJefe().getId());
        assertEquals(1, result.getEstudiantes().size());
        assertEquals("Ana", result.getEstudiantes().get(0).getNombre());
    }
}