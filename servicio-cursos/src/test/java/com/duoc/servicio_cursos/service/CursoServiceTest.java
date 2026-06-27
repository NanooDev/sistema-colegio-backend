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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CursoServiceTest {

    @Mock
    private CursoEntityRepository cursoEntityRepository;

    @Mock
    private ProfesorFeign profesorFeign;

    @Mock
    private EstudianteFeign estudianteFeign;

    @InjectMocks
    private CursoService cursoService;

    // --- listarCursos ---

    @Test
    void listarCursos_cuandoExisten_deberiaRetornarLista() {
        // Given
        CursoEntity entity = new CursoEntity();
        entity.setId(1L);
        entity.setNombre("1ro Basico A");
        entity.setProfesorJefeId(5L);

        when(cursoEntityRepository.findAll()).thenReturn(List.of(entity));

        // When
        List<Curso> resultado = cursoService.listarCursos();

        // Then
        assertEquals(1, resultado.size());
        assertEquals("1ro Basico A", resultado.get(0).getNombre());
        assertEquals(5L, resultado.get(0).getProfesorJefeId());
    }

    @Test
    void listarCursos_cuandoNoExisten_deberiaRetornarListaVacia() {
        // Given
        when(cursoEntityRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<Curso> resultado = cursoService.listarCursos();

        // Then
        assertTrue(resultado.isEmpty());
    }

    // --- crear ---

    @Test
    void crear_conDatosValidos_deberiaCrearCurso() {
        // Given
        CursoCreateRequest request = new CursoCreateRequest();
        request.setNombre("2do Basico B");
        request.setProfesorJefeId(3L);

        CursoEntity guardado = new CursoEntity();
        guardado.setId(1L);
        guardado.setNombre("2do Basico B");
        guardado.setProfesorJefeId(3L);

        when(cursoEntityRepository.save(any(CursoEntity.class))).thenReturn(guardado);

        // When
        Curso resultado = cursoService.crear(request);

        // Then
        assertNotNull(resultado);
        assertEquals("2do Basico B", resultado.getNombre());
        assertEquals(3L, resultado.getProfesorJefeId());
        verify(cursoEntityRepository).save(any(CursoEntity.class));
    }

    // --- actualizar ---

    @Test
    void actualizar_cuandoExiste_deberiaActualizarYRetornar() {
        // Given
        CursoEntity existente = new CursoEntity();
        existente.setId(1L);
        existente.setNombre("1ro Basico A");
        existente.setProfesorJefeId(5L);

        CursoCreateRequest request = new CursoCreateRequest();
        request.setNombre("1ro Basico B");
        request.setProfesorJefeId(7L);

        CursoEntity actualizado = new CursoEntity();
        actualizado.setId(1L);
        actualizado.setNombre("1ro Basico B");
        actualizado.setProfesorJefeId(7L);

        when(cursoEntityRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(cursoEntityRepository.save(any(CursoEntity.class))).thenReturn(actualizado);

        // When
        Curso resultado = cursoService.actualizar(1L, request);

        // Then
        assertEquals("1ro Basico B", resultado.getNombre());
        assertEquals(7L, resultado.getProfesorJefeId());
    }

    @Test
    void actualizar_cuandoNoExiste_deberiaLanzarExcepcion() {
        // Given
        CursoCreateRequest request = new CursoCreateRequest();
        request.setNombre("Test");
        request.setProfesorJefeId(1L);

        when(cursoEntityRepository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(CursoNotFoundException.class, () -> cursoService.actualizar(99L, request));
        verify(cursoEntityRepository, never()).save(any());
    }

    // --- eliminar ---

    @Test
    void eliminar_cuandoExiste_deberiaEliminar() {
        // Given
        CursoEntity entity = new CursoEntity();
        entity.setId(10L);

        when(cursoEntityRepository.findById(10L)).thenReturn(Optional.of(entity));
        doNothing().when(cursoEntityRepository).delete(entity);

        // When
        cursoService.eliminar(10L);

        // Then
        verify(cursoEntityRepository).delete(entity);
    }

    @Test
    void eliminar_cuandoNoExiste_deberiaLanzarExcepcion() {
        // Given
        when(cursoEntityRepository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(CursoNotFoundException.class, () -> cursoService.eliminar(99L));
    }

    // --- obtenerCursoConDetalles ---

    @Test
    void obtenerCursoConDetalles_cuandoExiste_deberiaRetornarCursoConProfesorYEstudiantes() {
        // Given
        CursoEntity entity = new CursoEntity();
        entity.setId(1L);
        entity.setNombre("1ro Basico A");
        entity.setProfesorJefeId(5L);

        ProfesorDTO profesorDTO = new ProfesorDTO();
        profesorDTO.setId(5L);
        profesorDTO.setNombre("Juan");
        profesorDTO.setApellido("Martinez");

        EstudianteDTO estudianteDTO = new EstudianteDTO();
        estudianteDTO.setId(1L);
        estudianteDTO.setNombre("Ana");

        when(cursoEntityRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(profesorFeign.obtenerProfesorPorId(5L)).thenReturn(profesorDTO);
        when(estudianteFeign.obtenerEstudiantesPorCurso(1L)).thenReturn(List.of(estudianteDTO));

        // When
        Curso resultado = cursoService.obtenerCursoConDetalles(1L);

        // Then
        assertNotNull(resultado);
        assertEquals("1ro Basico A", resultado.getNombre());
        assertNotNull(resultado.getProfesorJefe());
        assertEquals("Juan", resultado.getProfesorJefe().getNombre());
        assertNotNull(resultado.getEstudiantes());
        assertEquals(1, resultado.getEstudiantes().size());
    }

    @Test
    void obtenerCursoConDetalles_cuandoFeignFalla_deberiaRetornarCursoSinDetalles() {
        // Given
        CursoEntity entity = new CursoEntity();
        entity.setId(1L);
        entity.setNombre("1ro Basico A");
        entity.setProfesorJefeId(5L);

        when(cursoEntityRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(profesorFeign.obtenerProfesorPorId(5L)).thenThrow(new RuntimeException("Servicio no disponible"));
        when(estudianteFeign.obtenerEstudiantesPorCurso(1L)).thenThrow(new RuntimeException("Servicio no disponible"));

        // When
        Curso resultado = cursoService.obtenerCursoConDetalles(1L);

        // Then
        assertNotNull(resultado);
        assertEquals("1ro Basico A", resultado.getNombre());
        assertNull(resultado.getProfesorJefe());
        assertNull(resultado.getEstudiantes());
    }

    @Test
    void obtenerCursoConDetalles_cuandoNoExiste_deberiaLanzarExcepcion() {
        // Given
        when(cursoEntityRepository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(CursoNotFoundException.class, () -> cursoService.obtenerCursoConDetalles(99L));
    }
}
