package com.duoc.servicio_asistencias.service;

import com.duoc.servicio_asistencias.client.EstudianteFeign;
import com.duoc.servicio_asistencias.dto.AsistenciaDTO;
import com.duoc.servicio_asistencias.dto.AsistenciaRequest;
import com.duoc.servicio_asistencias.dto.EstudianteDTO;
import com.duoc.servicio_asistencias.exception.AsistenciaNotFoundException;
import com.duoc.servicio_asistencias.model.Asistencia;
import com.duoc.servicio_asistencias.repository.AsistenciaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AsistenciaServiceTest {

    @Mock
    private AsistenciaRepository repository;

    @Mock
    private EstudianteFeign estudianteFeign;

    @InjectMocks
    private AsistenciaService asistenciaService;

    // --- guardar ---

    @Test
    void guardar_conDatosValidos_deberiaCrearAsistencia() {
        // Given
        AsistenciaRequest request = new AsistenciaRequest();
        request.setEstudianteId(1L);
        request.setCursoId(2L);
        request.setFecha(LocalDate.of(2025, 6, 15));
        request.setPresente(true);

        Asistencia guardada = new Asistencia();
        guardada.setId(1L);
        guardada.setEstudianteId(1L);
        guardada.setCursoId(2L);
        guardada.setFecha(LocalDate.of(2025, 6, 15));
        guardada.setPresente(true);

        when(repository.save(any(Asistencia.class))).thenReturn(guardada);

        // When
        AsistenciaDTO resultado = asistenciaService.guardar(request);

        // Then
        assertNotNull(resultado);
        assertEquals(1L, resultado.getEstudianteId());
        assertEquals(2L, resultado.getCursoId());
        assertTrue(resultado.getPresente());
        assertEquals("asistencia", resultado.getCategoria());
        verify(repository).save(any(Asistencia.class));
    }

    @Test
    void guardar_conEstudianteAusente_deberiaRegistrarAusencia() {
        // Given
        AsistenciaRequest request = new AsistenciaRequest();
        request.setEstudianteId(3L);
        request.setCursoId(1L);
        request.setFecha(LocalDate.of(2025, 6, 20));
        request.setPresente(false);

        Asistencia guardada = new Asistencia();
        guardada.setId(2L);
        guardada.setEstudianteId(3L);
        guardada.setCursoId(1L);
        guardada.setFecha(LocalDate.of(2025, 6, 20));
        guardada.setPresente(false);

        when(repository.save(any(Asistencia.class))).thenReturn(guardada);

        // When
        AsistenciaDTO resultado = asistenciaService.guardar(request);

        // Then
        assertNotNull(resultado);
        assertFalse(resultado.getPresente());
    }

    // --- listar ---

    @Test
    void listar_cuandoExistenRegistros_deberiaRetornarLista() {
        // Given
        Asistencia asistencia = new Asistencia();
        asistencia.setId(1L);
        asistencia.setEstudianteId(1L);
        asistencia.setCursoId(2L);
        asistencia.setFecha(LocalDate.of(2025, 6, 15));
        asistencia.setPresente(true);

        when(repository.findAll()).thenReturn(List.of(asistencia));

        // When
        List<AsistenciaDTO> resultado = asistenciaService.listar();

        // Then
        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getEstudianteId());
    }

    @Test
    void listar_cuandoNoExistenRegistros_deberiaRetornarListaVacia() {
        // Given
        when(repository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<AsistenciaDTO> resultado = asistenciaService.listar();

        // Then
        assertTrue(resultado.isEmpty());
    }

    // --- buscarPorId ---

    @Test
    void buscarPorId_cuandoExiste_deberiaRetornarDTO() {
        // Given
        Asistencia asistencia = new Asistencia();
        asistencia.setId(5L);
        asistencia.setEstudianteId(2L);
        asistencia.setCursoId(3L);
        asistencia.setFecha(LocalDate.of(2025, 6, 10));
        asistencia.setPresente(true);

        when(repository.findById(5L)).thenReturn(Optional.of(asistencia));

        // When
        AsistenciaDTO resultado = asistenciaService.buscarPorId(5L);

        // Then
        assertNotNull(resultado);
        assertEquals(5L, resultado.getId());
        assertEquals(2L, resultado.getEstudianteId());
    }

    @Test
    void buscarPorId_cuandoNoExiste_deberiaLanzarExcepcion() {
        // Given
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(AsistenciaNotFoundException.class, () -> asistenciaService.buscarPorId(99L));
    }

    // --- actualizar ---

    @Test
    void actualizar_cuandoExiste_deberiaActualizarYRetornarDTO() {
        // Given
        Asistencia existente = new Asistencia();
        existente.setId(1L);
        existente.setEstudianteId(1L);
        existente.setCursoId(2L);
        existente.setFecha(LocalDate.of(2025, 6, 15));
        existente.setPresente(false);

        AsistenciaRequest request = new AsistenciaRequest();
        request.setEstudianteId(1L);
        request.setCursoId(2L);
        request.setFecha(LocalDate.of(2025, 6, 15));
        request.setPresente(true);

        Asistencia actualizada = new Asistencia();
        actualizada.setId(1L);
        actualizada.setEstudianteId(1L);
        actualizada.setCursoId(2L);
        actualizada.setFecha(LocalDate.of(2025, 6, 15));
        actualizada.setPresente(true);

        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(any(Asistencia.class))).thenReturn(actualizada);

        // When
        AsistenciaDTO resultado = asistenciaService.actualizar(1L, request);

        // Then
        assertTrue(resultado.getPresente());
    }

    @Test
    void actualizar_cuandoNoExiste_deberiaLanzarExcepcion() {
        // Given
        AsistenciaRequest request = new AsistenciaRequest();
        request.setEstudianteId(1L);
        request.setCursoId(2L);
        request.setFecha(LocalDate.now());
        request.setPresente(true);

        when(repository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(AsistenciaNotFoundException.class, () -> asistenciaService.actualizar(99L, request));
        verify(repository, never()).save(any());
    }

    // --- eliminar ---

    @Test
    void eliminar_cuandoExiste_deberiaEliminar() {
        // Given
        Asistencia asistencia = new Asistencia();
        asistencia.setId(10L);

        when(repository.findById(10L)).thenReturn(Optional.of(asistencia));
        doNothing().when(repository).deleteById(10L);

        // When
        asistenciaService.eliminar(10L);

        // Then
        verify(repository).deleteById(10L);
    }

    @Test
    void eliminar_cuandoNoExiste_deberiaLanzarExcepcion() {
        // Given
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(AsistenciaNotFoundException.class, () -> asistenciaService.eliminar(99L));
        verify(repository, never()).deleteById(any());
    }

    // --- obtenerAsistenciaConEstudiante ---

    @Test
    void obtenerConEstudiante_cuandoExiste_deberiaEnriquecerConNombre() {
        // Given
        Asistencia asistencia = new Asistencia();
        asistencia.setId(1L);
        asistencia.setEstudianteId(5L);
        asistencia.setCursoId(2L);
        asistencia.setFecha(LocalDate.of(2025, 6, 15));
        asistencia.setPresente(true);

        EstudianteDTO estDTO = new EstudianteDTO();
        estDTO.setId(5L);
        estDTO.setNombre("Ana");
        estDTO.setApellido("Perez");

        when(repository.findById(1L)).thenReturn(Optional.of(asistencia));
        when(estudianteFeign.obtenerEstudiantePorId(5L)).thenReturn(estDTO);

        // When
        AsistenciaDTO resultado = asistenciaService.obtenerAsistenciaConEstudiante(1L);

        // Then
        assertNotNull(resultado);
        assertEquals("Ana Perez", resultado.getNombreEstudiante());
        verify(estudianteFeign).obtenerEstudiantePorId(5L);
    }

    @Test
    void obtenerConEstudiante_cuandoFeignFalla_deberiaRetornarSinNombre() {
        // Given
        Asistencia asistencia = new Asistencia();
        asistencia.setId(1L);
        asistencia.setEstudianteId(5L);
        asistencia.setCursoId(2L);
        asistencia.setFecha(LocalDate.of(2025, 6, 15));
        asistencia.setPresente(true);

        when(repository.findById(1L)).thenReturn(Optional.of(asistencia));
        when(estudianteFeign.obtenerEstudiantePorId(5L)).thenThrow(new RuntimeException("Servicio no disponible"));

        // When
        AsistenciaDTO resultado = asistenciaService.obtenerAsistenciaConEstudiante(1L);

        // Then
        assertNotNull(resultado);
        assertNull(resultado.getNombreEstudiante());
    }
}
