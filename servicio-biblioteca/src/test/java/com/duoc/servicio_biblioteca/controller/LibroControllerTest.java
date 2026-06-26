package com.duoc.servicio_biblioteca.controller;

import com.duoc.servicio_biblioteca.dto.LibroDTO;
import com.duoc.servicio_biblioteca.dto.LibroRequest;
import com.duoc.servicio_biblioteca.service.LibroService;
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

@WebMvcTest(LibroController.class)
class LibroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LibroService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /api/v1/biblioteca - Guardar libro retorna 201")
    void guardar_retorna201() throws Exception {
        LibroRequest request = new LibroRequest();
        request.setTitulo("Don Quijote de la Mancha");
        request.setAutor("Miguel de Cervantes");
        request.setEjemplaresDisponibles(5);

        LibroDTO dto = new LibroDTO();
        dto.setId(1L);
        dto.setTitulo("Don Quijote de la Mancha");
        dto.setAutor("Miguel de Cervantes");
        dto.setEjemplaresDisponibles(5);

        when(service.guardar(any(LibroRequest.class))).thenReturn(dto);

        mockMvc.perform(post("/api/v1/biblioteca")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.titulo").value("Don Quijote de la Mancha"))
                .andExpect(jsonPath("$.autor").value("Miguel de Cervantes"))
                .andExpect(jsonPath("$.ejemplaresDisponibles").value(5));
    }

    @Test
    @DisplayName("GET /api/v1/biblioteca - Listar libros retorna 200")
    void listar_retorna200() throws Exception {
        LibroDTO dto = new LibroDTO();
        dto.setId(1L);
        dto.setTitulo("Don Quijote de la Mancha");
        dto.setAutor("Miguel de Cervantes");
        dto.setEjemplaresDisponibles(5);

        when(service.listar()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/v1/biblioteca"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].titulo").value("Don Quijote de la Mancha"));
    }

    @Test
    @DisplayName("GET /api/v1/biblioteca - Lista vacia retorna 204")
    void listar_vacio_retorna204() throws Exception {
        when(service.listar()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/biblioteca"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /api/v1/biblioteca/{id} - Buscar por ID retorna 200")
    void buscarPorId_retorna200() throws Exception {
        LibroDTO dto = new LibroDTO();
        dto.setId(1L);
        dto.setTitulo("Don Quijote de la Mancha");
        dto.setAutor("Miguel de Cervantes");
        dto.setEjemplaresDisponibles(5);

        when(service.buscarPorId(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/v1/biblioteca/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.ejemplaresDisponibles").value(5));
    }

    @Test
    @DisplayName("PUT /api/v1/biblioteca/{id} - Actualizar libro retorna 200")
    void actualizar_retorna200() throws Exception {
        LibroRequest request = new LibroRequest();
        request.setTitulo("Don Quijote de la Mancha");
        request.setAutor("Miguel de Cervantes");
        request.setEjemplaresDisponibles(3);

        LibroDTO dto = new LibroDTO();
        dto.setId(1L);
        dto.setTitulo("Don Quijote de la Mancha");
        dto.setAutor("Miguel de Cervantes");
        dto.setEjemplaresDisponibles(3);

        when(service.actualizar(eq(1L), any(LibroRequest.class))).thenReturn(dto);

        mockMvc.perform(put("/api/v1/biblioteca/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ejemplaresDisponibles").value(3));
    }

    @Test
    @DisplayName("DELETE /api/v1/biblioteca/{id} - Eliminar libro retorna 204")
    void eliminar_retorna204() throws Exception {
        doNothing().when(service).eliminar(1L);

        mockMvc.perform(delete("/api/v1/biblioteca/1"))
                .andExpect(status().isNoContent());
    }
}
