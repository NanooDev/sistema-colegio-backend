package com.duoc.servicio_asignaturas.controller;

import com.duoc.servicio_asignaturas.dto.AsignaturaDTO;
import com.duoc.servicio_asignaturas.dto.AsignaturaRequest;
import com.duoc.servicio_asignaturas.service.AsignaturaService;
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

@WebMvcTest(AsignaturaController.class)
class AsignaturaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AsignaturaService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /api/v1/asignaturas - Guardar asignatura retorna 201")
    void guardar_retorna201() throws Exception {
        AsignaturaRequest request = new AsignaturaRequest();
        request.setNombre("Algebra Lineal");
        request.setCodigo("MAT-201");

        AsignaturaDTO dto = new AsignaturaDTO();
        dto.setId(1L);
        dto.setNombre("Algebra Lineal");
        dto.setCodigo("MAT-201");

        when(service.guardar(any(AsignaturaRequest.class))).thenReturn(dto);

        mockMvc.perform(post("/api/v1/asignaturas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Algebra Lineal"))
                .andExpect(jsonPath("$.codigo").value("MAT-201"));
    }

    @Test
    @DisplayName("GET /api/v1/asignaturas - Listar asignaturas retorna 200")
    void listar_retorna200() throws Exception {
        AsignaturaDTO dto = new AsignaturaDTO();
        dto.setId(1L);
        dto.setNombre("Algebra Lineal");
        dto.setCodigo("MAT-201");

        when(service.listar()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/v1/asignaturas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Algebra Lineal"));
    }

    @Test
    @DisplayName("GET /api/v1/asignaturas - Lista vacia retorna 204")
    void listar_vacio_retorna204() throws Exception {
        when(service.listar()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/asignaturas"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /api/v1/asignaturas/{id} - Buscar por ID retorna 200")
    void buscarPorId_retorna200() throws Exception {
        AsignaturaDTO dto = new AsignaturaDTO();
        dto.setId(1L);
        dto.setNombre("Algebra Lineal");
        dto.setCodigo("MAT-201");

        when(service.buscarPorId(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/v1/asignaturas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.codigo").value("MAT-201"));
    }

    @Test
    @DisplayName("PUT /api/v1/asignaturas/{id} - Actualizar asignatura retorna 200")
    void actualizar_retorna200() throws Exception {
        AsignaturaRequest request = new AsignaturaRequest();
        request.setNombre("Calculo Integral");
        request.setCodigo("MAT-302");

        AsignaturaDTO dto = new AsignaturaDTO();
        dto.setId(1L);
        dto.setNombre("Calculo Integral");
        dto.setCodigo("MAT-302");

        when(service.actualizar(eq(1L), any(AsignaturaRequest.class))).thenReturn(dto);

        mockMvc.perform(put("/api/v1/asignaturas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Calculo Integral"))
                .andExpect(jsonPath("$.codigo").value("MAT-302"));
    }

    @Test
    @DisplayName("DELETE /api/v1/asignaturas/{id} - Eliminar asignatura retorna 204")
    void eliminar_retorna204() throws Exception {
        doNothing().when(service).eliminar(1L);

        mockMvc.perform(delete("/api/v1/asignaturas/1"))
                .andExpect(status().isNoContent());
    }
}
