package com.duoc.servicio_cursos.controller;

import com.duoc.servicio_cursos.dto.CursoCreateRequest;
import com.duoc.servicio_cursos.model.Curso;
import com.duoc.servicio_cursos.service.CursoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CursoController.class)
class CursoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CursoService cursoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /api/v1/cursos - Crear curso retorna 201")
    void crear_retorna201() throws Exception {
        CursoCreateRequest request = new CursoCreateRequest();
        request.setNombre("Octavo Basico A");
        request.setProfesorJefeId(3L);

        Curso curso = new Curso();
        curso.setId(1L);
        curso.setNombre("Octavo Basico A");
        curso.setProfesorJefeId(3L);

        when(cursoService.crear(any(CursoCreateRequest.class))).thenReturn(curso);

        mockMvc.perform(post("/api/v1/cursos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Octavo Basico A"))
                .andExpect(jsonPath("$.profesorJefeId").value(3));
    }

    @Test
    @DisplayName("GET /api/v1/cursos - Listar cursos retorna 200")
    void listarCursos_retorna200() throws Exception {
        Curso curso = new Curso();
        curso.setId(1L);
        curso.setNombre("Octavo Basico A");
        curso.setProfesorJefeId(3L);

        when(cursoService.listarCursos()).thenReturn(List.of(curso));

        mockMvc.perform(get("/api/v1/cursos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Octavo Basico A"));
    }

    @Test
    @DisplayName("GET /api/v1/cursos/{id} - Obtener curso con detalles retorna 200")
    void obtenerCursoConDetalles_retorna200() throws Exception {
        Curso curso = new Curso();
        curso.setId(1L);
        curso.setNombre("Octavo Basico A");
        curso.setProfesorJefeId(3L);

        when(cursoService.obtenerCursoConDetalles(1L)).thenReturn(curso);

        mockMvc.perform(get("/api/v1/cursos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Octavo Basico A"))
                .andExpect(jsonPath("$.profesorJefeId").value(3));
    }

    @Test
    @DisplayName("PUT /api/v1/cursos/{id} - Actualizar curso retorna 200")
    void actualizar_retorna200() throws Exception {
        CursoCreateRequest request = new CursoCreateRequest();
        request.setNombre("Octavo Basico B");
        request.setProfesorJefeId(7L);

        Curso curso = new Curso();
        curso.setId(1L);
        curso.setNombre("Octavo Basico B");
        curso.setProfesorJefeId(7L);

        when(cursoService.actualizar(eq(1L), any(CursoCreateRequest.class))).thenReturn(curso);

        mockMvc.perform(put("/api/v1/cursos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Octavo Basico B"))
                .andExpect(jsonPath("$.profesorJefeId").value(7));
    }

    @Test
    @DisplayName("DELETE /api/v1/cursos/{id} - Eliminar curso retorna 204")
    void eliminar_retorna204() throws Exception {
        doNothing().when(cursoService).eliminar(1L);

        mockMvc.perform(delete("/api/v1/cursos/1"))
                .andExpect(status().isNoContent());
    }
}
