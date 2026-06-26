package com.duoc.servicio_profesores.controller;

import com.duoc.servicio_profesores.dto.ProfesorDTO;
import com.duoc.servicio_profesores.dto.ProfesorRequest;
import com.duoc.servicio_profesores.service.ProfesorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProfesorController.class)
class ProfesorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProfesorService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /api/v1/profesores - Guardar profesor retorna 201")
    void guardar_retorna201() throws Exception {
        ProfesorRequest request = new ProfesorRequest();
        request.setNombre("Carlos");
        request.setApellido("Ramirez");
        request.setEspecialidad("Matematicas");

        ProfesorDTO dto = new ProfesorDTO();
        dto.setId(1L);
        dto.setNombre("Carlos");
        dto.setApellido("Ramirez");
        dto.setEspecialidad("Matematicas");

        when(service.guardar(any(ProfesorRequest.class))).thenReturn(dto);

        mockMvc.perform(post("/api/v1/profesores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Carlos"))
                .andExpect(jsonPath("$.apellido").value("Ramirez"))
                .andExpect(jsonPath("$.especialidad").value("Matematicas"));
    }

    @Test
    @DisplayName("GET /api/v1/profesores - Listar profesores retorna 200")
    void listar_retorna200() throws Exception {
        ProfesorDTO dto = new ProfesorDTO();
        dto.setId(1L);
        dto.setNombre("Carlos");
        dto.setApellido("Ramirez");
        dto.setEspecialidad("Matematicas");

        when(service.listar()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/v1/profesores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Carlos"));
    }

    @Test
    @DisplayName("GET /api/v1/profesores - Lista vacia retorna 204")
    void listar_vacio_retorna204() throws Exception {
        when(service.listar()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/profesores"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /api/v1/profesores/{id} - Buscar por ID retorna 200")
    void buscarPorId_retorna200() throws Exception {
        ProfesorDTO dto = new ProfesorDTO();
        dto.setId(1L);
        dto.setNombre("Carlos");
        dto.setApellido("Ramirez");
        dto.setEspecialidad("Matematicas");

        when(service.buscarPorId(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/v1/profesores/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.especialidad").value("Matematicas"));
    }

    @Test
    @DisplayName("PUT /api/v1/profesores/{id} - Actualizar profesor retorna 200")
    void actualizar_retorna200() throws Exception {
        ProfesorRequest request = new ProfesorRequest();
        request.setNombre("Carlos");
        request.setApellido("Ramirez");
        request.setEspecialidad("Fisica");

        ProfesorDTO dto = new ProfesorDTO();
        dto.setId(1L);
        dto.setNombre("Carlos");
        dto.setApellido("Ramirez");
        dto.setEspecialidad("Fisica");

        when(service.actualizar(eq(1L), any(ProfesorRequest.class))).thenReturn(dto);

        mockMvc.perform(put("/api/v1/profesores/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.especialidad").value("Fisica"));
    }

    @Test
    @DisplayName("DELETE /api/v1/profesores/{id} - Eliminar profesor retorna 204")
    void eliminar_retorna204() throws Exception {
        doNothing().when(service).eliminar(1L);

        mockMvc.perform(delete("/api/v1/profesores/1"))
                .andExpect(status().isNoContent());
    }
}
