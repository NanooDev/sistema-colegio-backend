package com.duoc.servicio_asistencias.controller;

import com.duoc.servicio_asistencias.dto.AsistenciaDTO;
import com.duoc.servicio_asistencias.dto.AsistenciaRequest;
import com.duoc.servicio_asistencias.service.AsistenciaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AsistenciaController.class)
class AsistenciaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AsistenciaService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /api/v1/asistencias - Guardar asistencia retorna 201")
    void guardar_retorna201() throws Exception {
        AsistenciaRequest request = new AsistenciaRequest();
        request.setEstudianteId(10L);
        request.setCursoId(3L);
        request.setFecha(LocalDate.of(2026, 6, 20));
        request.setPresente(true);

        AsistenciaDTO dto = new AsistenciaDTO();
        dto.setId(1L);
        dto.setEstudianteId(10L);
        dto.setCursoId(3L);
        dto.setFecha(LocalDate.of(2026, 6, 20));
        dto.setPresente(true);

        when(service.guardar(any(AsistenciaRequest.class))).thenReturn(dto);

        mockMvc.perform(post("/api/v1/asistencias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.estudianteId").value(10))
                .andExpect(jsonPath("$.cursoId").value(3))
                .andExpect(jsonPath("$.presente").value(true));
    }

    @Test
    @DisplayName("GET /api/v1/asistencias - Listar asistencias retorna 200")
    void listar_retorna200() throws Exception {
        AsistenciaDTO dto = new AsistenciaDTO();
        dto.setId(1L);
        dto.setEstudianteId(10L);
        dto.setCursoId(3L);
        dto.setFecha(LocalDate.of(2026, 6, 20));
        dto.setPresente(true);

        when(service.listar()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/v1/asistencias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].estudianteId").value(10));
    }

    @Test
    @DisplayName("GET /api/v1/asistencias - Lista vacia retorna 204")
    void listar_vacio_retorna204() throws Exception {
        when(service.listar()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/asistencias"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /api/v1/asistencias/{id} - Buscar por ID retorna 200")
    void buscarPorId_retorna200() throws Exception {
        AsistenciaDTO dto = new AsistenciaDTO();
        dto.setId(1L);
        dto.setEstudianteId(10L);
        dto.setCursoId(3L);
        dto.setFecha(LocalDate.of(2026, 6, 20));
        dto.setPresente(true);

        when(service.buscarPorId(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/v1/asistencias/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.presente").value(true));
    }

    @Test
    @DisplayName("PUT /api/v1/asistencias/{id} - Actualizar asistencia retorna 200")
    void actualizar_retorna200() throws Exception {
        AsistenciaRequest request = new AsistenciaRequest();
        request.setEstudianteId(10L);
        request.setCursoId(3L);
        request.setFecha(LocalDate.of(2026, 6, 20));
        request.setPresente(false);

        AsistenciaDTO dto = new AsistenciaDTO();
        dto.setId(1L);
        dto.setEstudianteId(10L);
        dto.setCursoId(3L);
        dto.setFecha(LocalDate.of(2026, 6, 20));
        dto.setPresente(false);

        when(service.actualizar(eq(1L), any(AsistenciaRequest.class))).thenReturn(dto);

        mockMvc.perform(put("/api/v1/asistencias/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.presente").value(false));
    }

    @Test
    @DisplayName("DELETE /api/v1/asistencias/{id} - Eliminar asistencia retorna 204")
    void eliminar_retorna204() throws Exception {
        doNothing().when(service).eliminar(1L);

        mockMvc.perform(delete("/api/v1/asistencias/1"))
                .andExpect(status().isNoContent());
    }
}
