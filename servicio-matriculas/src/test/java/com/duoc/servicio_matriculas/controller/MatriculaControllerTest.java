package com.duoc.servicio_matriculas.controller;

import com.duoc.servicio_matriculas.dto.MatriculaDTO;
import com.duoc.servicio_matriculas.dto.MatriculaRequest;
import com.duoc.servicio_matriculas.service.MatriculaService;
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

@WebMvcTest(MatriculaController.class)
class MatriculaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MatriculaService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /api/v1/matriculas - Guardar matricula retorna 201")
    void guardar_retorna201() throws Exception {
        MatriculaRequest request = new MatriculaRequest();
        request.setEstudianteId(12L);
        request.setCursoId(4L);
        request.setAnioEscolar("2026");

        MatriculaDTO dto = new MatriculaDTO();
        dto.setId(1L);
        dto.setEstudianteId(12L);
        dto.setCursoId(4L);
        dto.setAnioEscolar("2026");

        when(service.guardar(any(MatriculaRequest.class))).thenReturn(dto);

        mockMvc.perform(post("/api/v1/matriculas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.estudianteId").value(12))
                .andExpect(jsonPath("$.cursoId").value(4))
                .andExpect(jsonPath("$.anioEscolar").value("2026"));
    }

    @Test
    @DisplayName("GET /api/v1/matriculas - Listar matriculas retorna 200")
    void listar_retorna200() throws Exception {
        MatriculaDTO dto = new MatriculaDTO();
        dto.setId(1L);
        dto.setEstudianteId(12L);
        dto.setCursoId(4L);
        dto.setAnioEscolar("2026");

        when(service.listar()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/v1/matriculas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].anioEscolar").value("2026"));
    }

    @Test
    @DisplayName("GET /api/v1/matriculas - Lista vacia retorna 204")
    void listar_vacio_retorna204() throws Exception {
        when(service.listar()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/matriculas"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /api/v1/matriculas/{id} - Buscar por ID retorna 200")
    void buscarPorId_retorna200() throws Exception {
        MatriculaDTO dto = new MatriculaDTO();
        dto.setId(1L);
        dto.setEstudianteId(12L);
        dto.setCursoId(4L);
        dto.setAnioEscolar("2026");

        when(service.buscarPorId(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/v1/matriculas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.estudianteId").value(12));
    }

    @Test
    @DisplayName("PUT /api/v1/matriculas/{id} - Actualizar matricula retorna 200")
    void actualizar_retorna200() throws Exception {
        MatriculaRequest request = new MatriculaRequest();
        request.setEstudianteId(12L);
        request.setCursoId(5L);
        request.setAnioEscolar("2027");

        MatriculaDTO dto = new MatriculaDTO();
        dto.setId(1L);
        dto.setEstudianteId(12L);
        dto.setCursoId(5L);
        dto.setAnioEscolar("2027");

        when(service.actualizar(eq(1L), any(MatriculaRequest.class))).thenReturn(dto);

        mockMvc.perform(put("/api/v1/matriculas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cursoId").value(5))
                .andExpect(jsonPath("$.anioEscolar").value("2027"));
    }

    @Test
    @DisplayName("DELETE /api/v1/matriculas/{id} - Eliminar matricula retorna 204")
    void eliminar_retorna204() throws Exception {
        doNothing().when(service).eliminar(1L);

        mockMvc.perform(delete("/api/v1/matriculas/1"))
                .andExpect(status().isNoContent());
    }
}
