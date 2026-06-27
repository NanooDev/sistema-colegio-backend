package com.duoc.servicio_finanzas.service;

import com.duoc.servicio_finanzas.client.EstudianteFeign;
import com.duoc.servicio_finanzas.dto.CargoDTO;
import com.duoc.servicio_finanzas.dto.CargoRequest;
import com.duoc.servicio_finanzas.dto.EstudianteDTO;
import com.duoc.servicio_finanzas.exception.CargoNotFoundException;
import com.duoc.servicio_finanzas.model.Cargo;
import com.duoc.servicio_finanzas.repository.CargoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CargoServiceTest {

    @Mock
    private CargoRepository repository;

    @Mock
    private EstudianteFeign estudianteFeign;

    @InjectMocks
    private CargoService cargoService;

    // --- guardar ---

    @Test
    void guardar_conDatosValidos_deberiaCrearCargo() {
        // Given
        CargoRequest request = new CargoRequest();
        request.setEstudianteId(1L);
        request.setConcepto("Mensualidad Marzo");
        request.setMonto(new BigDecimal("150000"));
        request.setFechaPago(LocalDate.of(2025, 3, 15));

        Cargo guardado = new Cargo();
        guardado.setId(1L);
        guardado.setEstudianteId(1L);
        guardado.setConcepto("Mensualidad Marzo");
        guardado.setMonto(new BigDecimal("150000"));
        guardado.setFechaPago(LocalDate.of(2025, 3, 15));

        when(repository.save(any(Cargo.class))).thenReturn(guardado);

        // When
        CargoDTO resultado = cargoService.guardar(request);

        // Then
        assertNotNull(resultado);
        assertEquals(1L, resultado.getEstudianteId());
        assertEquals("Mensualidad Marzo", resultado.getConcepto());
        assertEquals(new BigDecimal("150000"), resultado.getMonto());
        assertEquals("cargo", resultado.getCategoria());
        verify(repository).save(any(Cargo.class));
    }

    // --- listar ---

    @Test
    void listar_cuandoExistenCargos_deberiaRetornarLista() {
        // Given
        Cargo cargo = new Cargo();
        cargo.setId(1L);
        cargo.setEstudianteId(1L);
        cargo.setConcepto("Matricula");
        cargo.setMonto(new BigDecimal("50000"));
        cargo.setFechaPago(LocalDate.of(2025, 1, 10));

        when(repository.findAll()).thenReturn(List.of(cargo));

        // When
        List<CargoDTO> resultado = cargoService.listar();

        // Then
        assertEquals(1, resultado.size());
        assertEquals("Matricula", resultado.get(0).getConcepto());
    }

    @Test
    void listar_cuandoNoExistenCargos_deberiaRetornarListaVacia() {
        // Given
        when(repository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<CargoDTO> resultado = cargoService.listar();

        // Then
        assertTrue(resultado.isEmpty());
    }

    // --- buscarPorId ---

    @Test
    void buscarPorId_cuandoExiste_deberiaRetornarDTO() {
        // Given
        Cargo cargo = new Cargo();
        cargo.setId(5L);
        cargo.setEstudianteId(2L);
        cargo.setConcepto("Seguro Escolar");
        cargo.setMonto(new BigDecimal("25000"));
        cargo.setFechaPago(LocalDate.of(2025, 2, 20));

        when(repository.findById(5L)).thenReturn(Optional.of(cargo));

        // When
        CargoDTO resultado = cargoService.buscarPorId(5L);

        // Then
        assertNotNull(resultado);
        assertEquals(5L, resultado.getId());
        assertEquals("Seguro Escolar", resultado.getConcepto());
    }

    @Test
    void buscarPorId_cuandoNoExiste_deberiaLanzarExcepcion() {
        // Given
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(CargoNotFoundException.class, () -> cargoService.buscarPorId(99L));
    }

    // --- actualizar ---

    @Test
    void actualizar_cuandoExiste_deberiaActualizarYRetornarDTO() {
        // Given
        Cargo existente = new Cargo();
        existente.setId(1L);
        existente.setEstudianteId(1L);
        existente.setConcepto("Mensualidad Marzo");
        existente.setMonto(new BigDecimal("150000"));
        existente.setFechaPago(LocalDate.of(2025, 3, 15));

        CargoRequest request = new CargoRequest();
        request.setEstudianteId(1L);
        request.setConcepto("Mensualidad Abril");
        request.setMonto(new BigDecimal("160000"));
        request.setFechaPago(LocalDate.of(2025, 4, 15));

        Cargo actualizado = new Cargo();
        actualizado.setId(1L);
        actualizado.setEstudianteId(1L);
        actualizado.setConcepto("Mensualidad Abril");
        actualizado.setMonto(new BigDecimal("160000"));
        actualizado.setFechaPago(LocalDate.of(2025, 4, 15));

        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(any(Cargo.class))).thenReturn(actualizado);

        // When
        CargoDTO resultado = cargoService.actualizar(1L, request);

        // Then
        assertEquals("Mensualidad Abril", resultado.getConcepto());
        assertEquals(new BigDecimal("160000"), resultado.getMonto());
    }

    @Test
    void actualizar_cuandoNoExiste_deberiaLanzarExcepcion() {
        // Given
        CargoRequest request = new CargoRequest();
        request.setEstudianteId(1L);
        request.setConcepto("Test");
        request.setMonto(new BigDecimal("1000"));
        request.setFechaPago(LocalDate.now());

        when(repository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(CargoNotFoundException.class, () -> cargoService.actualizar(99L, request));
        verify(repository, never()).save(any());
    }

    // --- eliminar ---

    @Test
    void eliminar_cuandoExiste_deberiaEliminar() {
        // Given
        Cargo cargo = new Cargo();
        cargo.setId(10L);

        when(repository.findById(10L)).thenReturn(Optional.of(cargo));
        doNothing().when(repository).deleteById(10L);

        // When
        cargoService.eliminar(10L);

        // Then
        verify(repository).deleteById(10L);
    }

    @Test
    void eliminar_cuandoNoExiste_deberiaLanzarExcepcion() {
        // Given
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(CargoNotFoundException.class, () -> cargoService.eliminar(99L));
        verify(repository, never()).deleteById(any());
    }

    // --- obtenerCargoConEstudiante ---

    @Test
    void obtenerConEstudiante_cuandoExiste_deberiaEnriquecerConNombre() {
        // Given
        Cargo cargo = new Cargo();
        cargo.setId(1L);
        cargo.setEstudianteId(5L);
        cargo.setConcepto("Mensualidad Marzo");
        cargo.setMonto(new BigDecimal("150000"));
        cargo.setFechaPago(LocalDate.of(2025, 3, 15));

        EstudianteDTO estDTO = new EstudianteDTO();
        estDTO.setId(5L);
        estDTO.setNombre("Ana");
        estDTO.setApellido("Perez");

        when(repository.findById(1L)).thenReturn(Optional.of(cargo));
        when(estudianteFeign.obtenerEstudiantePorId(5L)).thenReturn(estDTO);

        // When
        CargoDTO resultado = cargoService.obtenerCargoConEstudiante(1L);

        // Then
        assertNotNull(resultado);
        assertEquals("Ana Perez", resultado.getNombreEstudiante());
        assertEquals("Mensualidad Marzo", resultado.getConcepto());
        verify(estudianteFeign).obtenerEstudiantePorId(5L);
    }

    @Test
    void obtenerConEstudiante_cuandoFeignFalla_deberiaRetornarSinNombre() {
        // Given
        Cargo cargo = new Cargo();
        cargo.setId(1L);
        cargo.setEstudianteId(5L);
        cargo.setConcepto("Mensualidad");
        cargo.setMonto(new BigDecimal("150000"));
        cargo.setFechaPago(LocalDate.of(2025, 3, 15));

        when(repository.findById(1L)).thenReturn(Optional.of(cargo));
        when(estudianteFeign.obtenerEstudiantePorId(5L)).thenThrow(new RuntimeException("Servicio no disponible"));

        // When
        CargoDTO resultado = cargoService.obtenerCargoConEstudiante(1L);

        // Then
        assertNotNull(resultado);
        assertNull(resultado.getNombreEstudiante());
    }
}
