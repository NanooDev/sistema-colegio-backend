package com.duoc.servicio_calificaciones.controller;

import com.duoc.servicio_calificaciones.dto.CalificacionDTO;
import com.duoc.servicio_calificaciones.dto.CalificacionRequest;
import com.duoc.servicio_calificaciones.service.CalificacionesService;
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

@WebMvcTest(CalificacionesController.class)
class CalificacionesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CalificacionesService calificacionesService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /api/v1/calificaciones - Guardar calificacion retorna 201")
    void guardar_retorna201() throws Exception {
        CalificacionRequest request = new CalificacionRequest();
        request.setEstudianteId(15);
        request.setCursoId(2);
        request.setNota1(6.5);
        request.setNota2(5.8);
        request.setNota3(7.0);
        request.setFecha(LocalDate.of(2026, 6, 15));

        CalificacionDTO dto = new CalificacionDTO();
        dto.setId(1);
        dto.setEstudianteId(15);
        dto.setCursoId(2);
        dto.setNota1(6.5);
        dto.setNota2(5.8);
        dto.setNota3(7.0);
        dto.setNotaFinal(6.43);
        dto.setEstado("APROBADO");
        dto.setFecha(LocalDate.of(2026, 6, 15));

        when(calificacionesService.guardar(any(CalificacionRequest.class))).thenReturn(dto);

        mockMvc.perform(post("/api/v1/calificaciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.estudianteId").value(15))
                .andExpect(jsonPath("$.cursoId").value(2))
                .andExpect(jsonPath("$.nota1").value(6.5))
                .andExpect(jsonPath("$.estado").value("APROBADO"));
    }

    @Test
    @DisplayName("GET /api/v1/calificaciones - Listar calificaciones retorna 200")
    void listar_retorna200() throws Exception {
        CalificacionDTO dto = new CalificacionDTO();
        dto.setId(1);
        dto.setEstudianteId(15);
        dto.setCursoId(2);
        dto.setNota1(6.5);
        dto.setNota2(5.8);
        dto.setNota3(7.0);
        dto.setNotaFinal(6.43);
        dto.setEstado("APROBADO");
        dto.setFecha(LocalDate.of(2026, 6, 15));

        when(calificacionesService.listar()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/v1/calificaciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].estudianteId").value(15));
    }

    @Test
    @DisplayName("GET /api/v1/calificaciones - Lista vacia retorna 204")
    void listar_vacio_retorna204() throws Exception {
        when(calificacionesService.listar()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/calificaciones"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /api/v1/calificaciones/{id} - Buscar por ID retorna 200")
    void buscarPorId_retorna200() throws Exception {
        CalificacionDTO dto = new CalificacionDTO();
        dto.setId(1);
        dto.setEstudianteId(15);
        dto.setCursoId(2);
        dto.setNota1(6.5);
        dto.setNota2(5.8);
        dto.setNota3(7.0);
        dto.setNotaFinal(6.43);
        dto.setEstado("APROBADO");
        dto.setFecha(LocalDate.of(2026, 6, 15));

        when(calificacionesService.buscarPorId(1)).thenReturn(dto);

        mockMvc.perform(get("/api/v1/calificaciones/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.notaFinal").value(6.43));
    }

    @Test
    @DisplayName("GET /api/v1/calificaciones/estudiante/{estudianteId} - Buscar por estudiante retorna 200")
    void buscarPorEstudiante_retorna200() throws Exception {
        CalificacionDTO dto = new CalificacionDTO();
        dto.setId(1);
        dto.setEstudianteId(15);
        dto.setCursoId(2);
        dto.setNota1(6.5);
        dto.setNota2(5.8);
        dto.setNota3(7.0);
        dto.setNotaFinal(6.43);
        dto.setEstado("APROBADO");
        dto.setFecha(LocalDate.of(2026, 6, 15));

        when(calificacionesService.buscarPorEstudiante(15)).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/v1/calificaciones/estudiante/15"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].estudianteId").value(15));
    }

    @Test
    @DisplayName("GET /api/v1/calificaciones/estudiante/{estudianteId} - Sin resultados retorna 204")
    void buscarPorEstudiante_vacio_retorna204() throws Exception {
        when(calificacionesService.buscarPorEstudiante(99)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/calificaciones/estudiante/99"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /api/v1/calificaciones/curso/{cursoId} - Buscar por curso retorna 200")
    void buscarPorCurso_retorna200() throws Exception {
        CalificacionDTO dto = new CalificacionDTO();
        dto.setId(1);
        dto.setEstudianteId(15);
        dto.setCursoId(2);
        dto.setNota1(6.5);
        dto.setNota2(5.8);
        dto.setNota3(7.0);
        dto.setNotaFinal(6.43);
        dto.setEstado("APROBADO");
        dto.setFecha(LocalDate.of(2026, 6, 15));

        when(calificacionesService.buscarPorCurso(2)).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/v1/calificaciones/curso/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cursoId").value(2));
    }

    @Test
    @DisplayName("GET /api/v1/calificaciones/curso/{cursoId} - Sin resultados retorna 204")
    void buscarPorCurso_vacio_retorna204() throws Exception {
        when(calificacionesService.buscarPorCurso(99)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/calificaciones/curso/99"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("PUT /api/v1/calificaciones/{id} - Actualizar calificacion retorna 200")
    void actualizar_retorna200() throws Exception {
        CalificacionRequest request = new CalificacionRequest();
        request.setEstudianteId(15);
        request.setCursoId(2);
        request.setNota1(7.0);
        request.setNota2(6.0);
        request.setNota3(6.5);
        request.setFecha(LocalDate.of(2026, 6, 15));

        CalificacionDTO dto = new CalificacionDTO();
        dto.setId(1);
        dto.setEstudianteId(15);
        dto.setCursoId(2);
        dto.setNota1(7.0);
        dto.setNota2(6.0);
        dto.setNota3(6.5);
        dto.setNotaFinal(6.50);
        dto.setEstado("APROBADO");
        dto.setFecha(LocalDate.of(2026, 6, 15));

        when(calificacionesService.actualizar(eq(1), any(CalificacionRequest.class))).thenReturn(dto);

        mockMvc.perform(put("/api/v1/calificaciones/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nota1").value(7.0))
                .andExpect(jsonPath("$.notaFinal").value(6.50));
    }

    @Test
    @DisplayName("DELETE /api/v1/calificaciones/{id} - Eliminar calificacion retorna 204")
    void eliminar_retorna204() throws Exception {
        doNothing().when(calificacionesService).eliminar(1);

        mockMvc.perform(delete("/api/v1/calificaciones/1"))
                .andExpect(status().isNoContent());
    }
}
