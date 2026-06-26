package com.duoc.servicio_estudiantes.controller;

import com.duoc.servicio_estudiantes.dto.EstudianteDTO;
import com.duoc.servicio_estudiantes.dto.EstudianteRequest;
import com.duoc.servicio_estudiantes.service.EstudianteService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

@WebMvcTest(EstudianteController.class)
class EstudianteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EstudianteService estudianteService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void guardar_deberiaRetornar201() throws Exception {
        EstudianteRequest request = new EstudianteRequest();
        request.setRut("20123456-7");
        request.setNombre("Ana");
        request.setApellido("Pérez");
        request.setCursoId(1L);

        EstudianteDTO dto = new EstudianteDTO();
        dto.setId(1);
        dto.setRut("20123456-7");
        dto.setNombre("Ana");
        dto.setApellido("Pérez");
        dto.setCursoId(1L);

        when(estudianteService.guardar(any(EstudianteRequest.class))).thenReturn(dto);

        mockMvc.perform(post("/api/v1/estudiantes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Ana"))
                .andExpect(jsonPath("$.rut").value("20123456-7"));
    }

    @Test
    void listar_deberiaRetornar200ConLista() throws Exception {
        EstudianteDTO dto = new EstudianteDTO();
        dto.setId(1);
        dto.setNombre("Ana");
        dto.setApellido("Pérez");
        dto.setRut("20123456-7");
        dto.setCursoId(1L);

        when(estudianteService.listar()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/v1/estudiantes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Ana"));
    }

    @Test
    void listar_deberiaRetornar204SiVacio() throws Exception {
        when(estudianteService.listar()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/estudiantes"))
                .andExpect(status().isNoContent());
    }

    @Test
    void buscarPorId_deberiaRetornar200() throws Exception {
        EstudianteDTO dto = new EstudianteDTO();
        dto.setId(7);
        dto.setNombre("Luis");
        dto.setApellido("González");
        dto.setRut("21111222-3");
        dto.setCursoId(2L);

        when(estudianteService.buscarPorId(7)).thenReturn(dto);

        mockMvc.perform(get("/api/v1/estudiantes/7"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.nombre").value("Luis"));
    }

    @Test
    void buscarPorCurso_deberiaRetornar200ConLista() throws Exception {
        EstudianteDTO dto = new EstudianteDTO();
        dto.setId(1);
        dto.setNombre("Ana");
        dto.setApellido("Pérez");
        dto.setRut("11111111-1");
        dto.setCursoId(4L);

        when(estudianteService.listarPorCurso(4L)).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/v1/estudiantes/curso/4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cursoId").value(4));
    }

    @Test
    void actualizar_deberiaRetornar200() throws Exception {
        EstudianteRequest request = new EstudianteRequest();
        request.setRut("44444444-4");
        request.setNombre("María");
        request.setApellido("López");
        request.setCursoId(8L);

        EstudianteDTO dto = new EstudianteDTO();
        dto.setId(3);
        dto.setRut("44444444-4");
        dto.setNombre("María");
        dto.setApellido("López");
        dto.setCursoId(8L);

        when(estudianteService.actualizar(eq(3), any(EstudianteRequest.class))).thenReturn(dto);

        mockMvc.perform(put("/api/v1/estudiantes/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("María"))
                .andExpect(jsonPath("$.rut").value("44444444-4"));
    }

    @Test
    void eliminar_deberiaRetornar204() throws Exception {
        doNothing().when(estudianteService).eliminar(12);

        mockMvc.perform(delete("/api/v1/estudiantes/12"))
                .andExpect(status().isNoContent());
    }
}
