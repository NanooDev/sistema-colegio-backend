package com.duoc.servicio_finanzas.controller;

import com.duoc.servicio_finanzas.dto.CargoDTO;
import com.duoc.servicio_finanzas.dto.CargoRequest;
import com.duoc.servicio_finanzas.service.CargoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CargoController.class)
class CargoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CargoService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /api/v1/finanzas - Guardar cargo retorna 201")
    void guardar_retorna201() throws Exception {
        CargoRequest request = new CargoRequest();
        request.setEstudianteId(5L);
        request.setConcepto("Mensualidad Julio");
        request.setMonto(new BigDecimal("150000.00"));
        request.setFechaPago(LocalDate.of(2026, 7, 5));

        CargoDTO dto = new CargoDTO();
        dto.setId(1L);
        dto.setEstudianteId(5L);
        dto.setConcepto("Mensualidad Julio");
        dto.setMonto(new BigDecimal("150000.00"));
        dto.setFechaPago(LocalDate.of(2026, 7, 5));

        when(service.guardar(any(CargoRequest.class))).thenReturn(dto);

        mockMvc.perform(post("/api/v1/finanzas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.estudianteId").value(5))
                .andExpect(jsonPath("$.concepto").value("Mensualidad Julio"))
                .andExpect(jsonPath("$.monto").value(150000.00));
    }

    @Test
    @DisplayName("GET /api/v1/finanzas - Listar cargos retorna 200")
    void listar_retorna200() throws Exception {
        CargoDTO dto = new CargoDTO();
        dto.setId(1L);
        dto.setEstudianteId(5L);
        dto.setConcepto("Mensualidad Julio");
        dto.setMonto(new BigDecimal("150000.00"));
        dto.setFechaPago(LocalDate.of(2026, 7, 5));

        when(service.listar()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/v1/finanzas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].concepto").value("Mensualidad Julio"));
    }

    @Test
    @DisplayName("GET /api/v1/finanzas - Lista vacia retorna 204")
    void listar_vacio_retorna204() throws Exception {
        when(service.listar()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/finanzas"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /api/v1/finanzas/{id} - Buscar por ID retorna 200")
    void buscarPorId_retorna200() throws Exception {
        CargoDTO dto = new CargoDTO();
        dto.setId(1L);
        dto.setEstudianteId(5L);
        dto.setConcepto("Mensualidad Julio");
        dto.setMonto(new BigDecimal("150000.00"));
        dto.setFechaPago(LocalDate.of(2026, 7, 5));

        when(service.buscarPorId(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/v1/finanzas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.monto").value(150000.00));
    }

    @Test
    @DisplayName("PUT /api/v1/finanzas/{id} - Actualizar cargo retorna 200")
    void actualizar_retorna200() throws Exception {
        CargoRequest request = new CargoRequest();
        request.setEstudianteId(5L);
        request.setConcepto("Mensualidad Agosto");
        request.setMonto(new BigDecimal("160000.00"));
        request.setFechaPago(LocalDate.of(2026, 8, 5));

        CargoDTO dto = new CargoDTO();
        dto.setId(1L);
        dto.setEstudianteId(5L);
        dto.setConcepto("Mensualidad Agosto");
        dto.setMonto(new BigDecimal("160000.00"));
        dto.setFechaPago(LocalDate.of(2026, 8, 5));

        when(service.actualizar(eq(1L), any(CargoRequest.class))).thenReturn(dto);

        mockMvc.perform(put("/api/v1/finanzas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.concepto").value("Mensualidad Agosto"))
                .andExpect(jsonPath("$.monto").value(160000.00));
    }

    @Test
    @DisplayName("DELETE /api/v1/finanzas/{id} - Eliminar cargo retorna 204")
    void eliminar_retorna204() throws Exception {
        doNothing().when(service).eliminar(1L);

        mockMvc.perform(delete("/api/v1/finanzas/1"))
                .andExpect(status().isNoContent());
    }
}
