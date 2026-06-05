package com.duoc.servicio_matriculas.service;

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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MatriculaServiceTest {

    @Mock
    private MatriculaRepository repository;

    @InjectMocks
    private MatriculaService matriculaService;

    @Test
    void guardar_deberiaCrearYRetornarDTO() {
        MatriculaRequest request = new MatriculaRequest();
        request.setEstudianteId(1L);
        request.setCursoId(2L);
        request.setAnioEscolar("2026");

        Matricula saved = new Matricula();
        saved.setId(1L);
        saved.setEstudianteId(1L);
        saved.setCursoId(2L);
        saved.setAnioEscolar("2026");

        when(repository.save(any(Matricula.class))).thenReturn(saved);

        MatriculaDTO result = matriculaService.guardar(request);

        assertEquals(1L, result.getId());
        assertEquals("matricula", result.getCategoria());
    }

    @Test
    void listar_deberiaRetornarTodasLasMatriculas() {
        Matricula m = new Matricula();
        m.setId(1L);
        when(repository.findAll()).thenReturn(List.of(m));

        List<MatriculaDTO> result = matriculaService.listar();

        assertEquals(1, result.size());
    }

    @Test
    void buscarPorId_deberiaRetornarDTOCuandoExiste() {
        Matricula m = new Matricula();
        m.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(m));

        MatriculaDTO result = matriculaService.buscarPorId(1L);

        assertEquals(1L, result.getId());
    }

    @Test
    void buscarPorId_deberiaLanzarExcepcionCuandoNoExiste() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(MatriculaNotFoundException.class, () -> matriculaService.buscarPorId(1L));
    }

    @Test
    void actualizar_deberiaActualizarYRetornarDTO() {
        Matricula existente = new Matricula();
        existente.setId(1L);
        MatriculaRequest request = new MatriculaRequest();
        request.setEstudianteId(9L);
        request.setCursoId(8L);
        request.setAnioEscolar("2027");

        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(existente)).thenReturn(existente);

        MatriculaDTO result = matriculaService.actualizar(1L, request);

        assertEquals(9L, result.getEstudianteId());
        verify(repository).save(existente);
    }

    @Test
    void eliminar_deberiaBorrarSiExiste() {
        Matricula existente = new Matricula();
        existente.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(existente));

        matriculaService.eliminar(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void eliminar_deberiaLanzarExcepcionSiNoExiste() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(MatriculaNotFoundException.class, () -> matriculaService.eliminar(1L));
        verify(repository, never()).deleteById(any());
    }
}