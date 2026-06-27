package com.duoc.servicio_matriculas.service;

import com.duoc.servicio_matriculas.client.CursoFeign;
import com.duoc.servicio_matriculas.client.EstudianteFeign;
import com.duoc.servicio_matriculas.dto.CursoDTO;
import com.duoc.servicio_matriculas.dto.EstudianteDTO;
import com.duoc.servicio_matriculas.dto.MatriculaDTO;
import com.duoc.servicio_matriculas.dto.MatriculaRequest;
import com.duoc.servicio_matriculas.exception.MatriculaNotFoundException;
import com.duoc.servicio_matriculas.model.Matricula;
import com.duoc.servicio_matriculas.repository.MatriculaRepository;
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
class MatriculaServiceTest {

    @Mock
    private MatriculaRepository repository;

    @Mock
    private EstudianteFeign estudianteFeign;

    @Mock
    private CursoFeign cursoFeign;

    @InjectMocks
    private MatriculaService matriculaService;

    // --- guardar ---

    @Test
    void guardar_conDatosValidos_deberiaCrearMatricula() {
        // Given
        MatriculaRequest request = new MatriculaRequest();
        request.setEstudianteId(1L);
        request.setCursoId(2L);
        request.setAnioEscolar("2025");

        Matricula guardada = new Matricula();
        guardada.setId(1L);
        guardada.setEstudianteId(1L);
        guardada.setCursoId(2L);
        guardada.setAnioEscolar("2025");

        when(repository.save(any(Matricula.class))).thenReturn(guardada);

        // When
        MatriculaDTO resultado = matriculaService.guardar(request);

        // Then
        assertNotNull(resultado);
        assertEquals(1L, resultado.getEstudianteId());
        assertEquals(2L, resultado.getCursoId());
        assertEquals("2025", resultado.getAnioEscolar());
        assertEquals("matricula", resultado.getCategoria());
        verify(repository).save(any(Matricula.class));
    }

    // --- listar ---

    @Test
    void listar_cuandoExistenMatriculas_deberiaRetornarLista() {
        // Given
        Matricula mat = new Matricula();
        mat.setId(1L);
        mat.setEstudianteId(1L);
        mat.setCursoId(2L);
        mat.setAnioEscolar("2025");

        when(repository.findAll()).thenReturn(List.of(mat));

        // When
        List<MatriculaDTO> resultado = matriculaService.listar();

        // Then
        assertEquals(1, resultado.size());
        assertEquals("2025", resultado.get(0).getAnioEscolar());
    }

    @Test
    void listar_cuandoNoExistenMatriculas_deberiaRetornarListaVacia() {
        // Given
        when(repository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<MatriculaDTO> resultado = matriculaService.listar();

        // Then
        assertTrue(resultado.isEmpty());
    }

    // --- buscarPorId ---

    @Test
    void buscarPorId_cuandoExiste_deberiaRetornarDTO() {
        // Given
        Matricula mat = new Matricula();
        mat.setId(5L);
        mat.setEstudianteId(3L);
        mat.setCursoId(1L);
        mat.setAnioEscolar("2024");

        when(repository.findById(5L)).thenReturn(Optional.of(mat));

        // When
        MatriculaDTO resultado = matriculaService.buscarPorId(5L);

        // Then
        assertNotNull(resultado);
        assertEquals(5L, resultado.getId());
        assertEquals(3L, resultado.getEstudianteId());
    }

    @Test
    void buscarPorId_cuandoNoExiste_deberiaLanzarExcepcion() {
        // Given
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(MatriculaNotFoundException.class, () -> matriculaService.buscarPorId(99L));
    }

    // --- actualizar ---

    @Test
    void actualizar_cuandoExiste_deberiaActualizarYRetornarDTO() {
        // Given
        Matricula existente = new Matricula();
        existente.setId(1L);
        existente.setEstudianteId(1L);
        existente.setCursoId(2L);
        existente.setAnioEscolar("2024");

        MatriculaRequest request = new MatriculaRequest();
        request.setEstudianteId(1L);
        request.setCursoId(3L);
        request.setAnioEscolar("2025");

        Matricula actualizada = new Matricula();
        actualizada.setId(1L);
        actualizada.setEstudianteId(1L);
        actualizada.setCursoId(3L);
        actualizada.setAnioEscolar("2025");

        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(any(Matricula.class))).thenReturn(actualizada);

        // When
        MatriculaDTO resultado = matriculaService.actualizar(1L, request);

        // Then
        assertEquals(3L, resultado.getCursoId());
        assertEquals("2025", resultado.getAnioEscolar());
    }

    @Test
    void actualizar_cuandoNoExiste_deberiaLanzarExcepcion() {
        // Given
        MatriculaRequest request = new MatriculaRequest();
        request.setEstudianteId(1L);
        request.setCursoId(2L);
        request.setAnioEscolar("2025");

        when(repository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(MatriculaNotFoundException.class, () -> matriculaService.actualizar(99L, request));
        verify(repository, never()).save(any());
    }

    // --- eliminar ---

    @Test
    void eliminar_cuandoExiste_deberiaEliminar() {
        // Given
        Matricula mat = new Matricula();
        mat.setId(10L);

        when(repository.findById(10L)).thenReturn(Optional.of(mat));
        doNothing().when(repository).deleteById(10L);

        // When
        matriculaService.eliminar(10L);

        // Then
        verify(repository).deleteById(10L);
    }

    @Test
    void eliminar_cuandoNoExiste_deberiaLanzarExcepcion() {
        // Given
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(MatriculaNotFoundException.class, () -> matriculaService.eliminar(99L));
        verify(repository, never()).deleteById(any());
    }

    // --- obtenerMatriculaConDetalles ---

    @Test
    void obtenerConDetalles_cuandoExiste_deberiaEnriquecerConNombres() {
        // Given
        Matricula mat = new Matricula();
        mat.setId(1L);
        mat.setEstudianteId(5L);
        mat.setCursoId(3L);
        mat.setAnioEscolar("2025");

        EstudianteDTO estDTO = new EstudianteDTO();
        estDTO.setId(5L);
        estDTO.setNombre("Ana");
        estDTO.setApellido("Perez");

        CursoDTO cursoDTO = new CursoDTO();
        cursoDTO.setId(3L);
        cursoDTO.setNombre("1ro Basico A");

        when(repository.findById(1L)).thenReturn(Optional.of(mat));
        when(estudianteFeign.obtenerEstudiantePorId(5L)).thenReturn(estDTO);
        when(cursoFeign.obtenerCursoPorId(3L)).thenReturn(cursoDTO);

        // When
        MatriculaDTO resultado = matriculaService.obtenerMatriculaConDetalles(1L);

        // Then
        assertNotNull(resultado);
        assertEquals("Ana Perez", resultado.getNombreEstudiante());
        assertEquals("1ro Basico A", resultado.getNombreCurso());
        verify(estudianteFeign).obtenerEstudiantePorId(5L);
        verify(cursoFeign).obtenerCursoPorId(3L);
    }

    @Test
    void obtenerConDetalles_cuandoFeignFalla_deberiaRetornarSinNombres() {
        // Given
        Matricula mat = new Matricula();
        mat.setId(1L);
        mat.setEstudianteId(5L);
        mat.setCursoId(3L);
        mat.setAnioEscolar("2025");

        when(repository.findById(1L)).thenReturn(Optional.of(mat));
        when(estudianteFeign.obtenerEstudiantePorId(5L)).thenThrow(new RuntimeException("Servicio no disponible"));
        when(cursoFeign.obtenerCursoPorId(3L)).thenThrow(new RuntimeException("Servicio no disponible"));

        // When
        MatriculaDTO resultado = matriculaService.obtenerMatriculaConDetalles(1L);

        // Then
        assertNotNull(resultado);
        assertNull(resultado.getNombreEstudiante());
        assertNull(resultado.getNombreCurso());
    }
}
