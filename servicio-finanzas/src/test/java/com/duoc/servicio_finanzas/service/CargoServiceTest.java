package com.duoc.servicio_finanzas.service;

import com.duoc.servicio_finanzas.dto.CargoDTO;
import com.duoc.servicio_finanzas.dto.CargoRequest;
import com.duoc.servicio_finanzas.exception.CargoNotFoundException;
import com.duoc.servicio_finanzas.model.Cargo;
import com.duoc.servicio_finanzas.repository.CargoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CargoServiceTest {

    @Mock
    private CargoRepository repository;

    @InjectMocks
    private CargoService cargoService;

    @Test
    void guardar_deberiaCrearYRetornarDTO() {
        CargoRequest request = new CargoRequest();
        request.setEstudianteId(1L);
        request.setConcepto("Matrícula");
        request.setMonto(BigDecimal.valueOf(100000L));
        request.setFechaPago(LocalDate.of(2026, 6, 3));

        Cargo saved = new Cargo();
        saved.setId(1L);
        saved.setEstudianteId(1L);
        saved.setConcepto("Matrícula");
        saved.setMonto(BigDecimal.valueOf(100000L));
        saved.setFechaPago(LocalDate.of(2026, 6, 3));

        when(repository.save(any(Cargo.class))).thenReturn(saved);

        CargoDTO result = cargoService.guardar(request);

        assertEquals(1L, result.getId());
        assertEquals("cargo", result.getCategoria());
    }

    @Test
    void listar_deberiaRetornarTodosLosCargos() {
        Cargo cargo = new Cargo();
        cargo.setId(1L);
        when(repository.findAll()).thenReturn(List.of(cargo));

        List<CargoDTO> result = cargoService.listar();

        assertEquals(1, result.size());
    }

    @Test
    void buscarPorId_deberiaRetornarDTOCuandoExiste() {
        Cargo cargo = new Cargo();
        cargo.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(cargo));

        CargoDTO result = cargoService.buscarPorId(1L);

        assertEquals(1L, result.getId());
    }

    @Test
    void buscarPorId_deberiaLanzarExcepcionCuandoNoExiste() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CargoNotFoundException.class, () -> cargoService.buscarPorId(1L));
    }

    @Test
    void actualizar_deberiaActualizarYRetornarDTO() {
        Cargo existente = new Cargo();
        existente.setId(1L);
        CargoRequest request = new CargoRequest();
        request.setEstudianteId(9L);
        request.setConcepto("Cuota");
        request.setMonto(BigDecimal.valueOf(200000L));
        request.setFechaPago(LocalDate.of(2026, 6, 4));

        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(existente)).thenReturn(existente);

        CargoDTO result = cargoService.actualizar(1L, request);

        assertEquals(9L, result.getEstudianteId());
        verify(repository).save(existente);
    }

    @Test
    void eliminar_deberiaBorrarSiExiste() {
        Cargo existente = new Cargo();
        existente.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(existente));

        cargoService.eliminar(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void eliminar_deberiaLanzarExcepcionSiNoExiste() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CargoNotFoundException.class, () -> cargoService.eliminar(1L));
        verify(repository, never()).deleteById(any());
    }
}