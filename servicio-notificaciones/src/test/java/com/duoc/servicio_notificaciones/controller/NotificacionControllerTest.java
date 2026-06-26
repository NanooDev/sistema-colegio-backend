package com.duoc.servicio_notificaciones.controller;

import com.duoc.servicio_notificaciones.dto.NotificacionDTO;
import com.duoc.servicio_notificaciones.dto.NotificacionRequest;
import com.duoc.servicio_notificaciones.service.NotificacionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificacionController.class)
class NotificacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private NotificacionService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /api/v1/notificaciones - Guardar notificacion retorna 201")
    void guardar_retorna201() throws Exception {
        NotificacionRequest request = new NotificacionRequest();
        request.setDestinatario("juan.perez@colegio.cl");
        request.setAsunto("Reunion de apoderados");
        request.setMensaje("Se convoca a reunion el dia viernes 10 de julio a las 18:00 hrs.");
        request.setFechaEnvio(LocalDateTime.of(2026, 7, 1, 9, 0, 0));

        NotificacionDTO dto = new NotificacionDTO();
        dto.setId(1L);
        dto.setDestinatario("juan.perez@colegio.cl");
        dto.setAsunto("Reunion de apoderados");
        dto.setMensaje("Se convoca a reunion el dia viernes 10 de julio a las 18:00 hrs.");
        dto.setFechaEnvio(LocalDateTime.of(2026, 7, 1, 9, 0, 0));

        when(service.guardar(any(NotificacionRequest.class))).thenReturn(dto);

        mockMvc.perform(post("/api/v1/notificaciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.destinatario").value("juan.perez@colegio.cl"))
                .andExpect(jsonPath("$.asunto").value("Reunion de apoderados"));
    }

    @Test
    @DisplayName("GET /api/v1/notificaciones - Listar notificaciones retorna 200")
    void listar_retorna200() throws Exception {
        NotificacionDTO dto = new NotificacionDTO();
        dto.setId(1L);
        dto.setDestinatario("juan.perez@colegio.cl");
        dto.setAsunto("Reunion de apoderados");
        dto.setMensaje("Se convoca a reunion el dia viernes 10 de julio a las 18:00 hrs.");
        dto.setFechaEnvio(LocalDateTime.of(2026, 7, 1, 9, 0, 0));

        when(service.listar()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/v1/notificaciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].destinatario").value("juan.perez@colegio.cl"));
    }

    @Test
    @DisplayName("GET /api/v1/notificaciones - Lista vacia retorna 204")
    void listar_vacio_retorna204() throws Exception {
        when(service.listar()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/notificaciones"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /api/v1/notificaciones/{id} - Buscar por ID retorna 200")
    void buscarPorId_retorna200() throws Exception {
        NotificacionDTO dto = new NotificacionDTO();
        dto.setId(1L);
        dto.setDestinatario("juan.perez@colegio.cl");
        dto.setAsunto("Reunion de apoderados");
        dto.setMensaje("Se convoca a reunion el dia viernes 10 de julio a las 18:00 hrs.");
        dto.setFechaEnvio(LocalDateTime.of(2026, 7, 1, 9, 0, 0));

        when(service.buscarPorId(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/v1/notificaciones/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.asunto").value("Reunion de apoderados"));
    }

    @Test
    @DisplayName("PUT /api/v1/notificaciones/{id} - Actualizar notificacion retorna 200")
    void actualizar_retorna200() throws Exception {
        NotificacionRequest request = new NotificacionRequest();
        request.setDestinatario("juan.perez@colegio.cl");
        request.setAsunto("Reunion de apoderados - ACTUALIZADA");
        request.setMensaje("La reunion se ha reprogramado para el lunes 13 de julio a las 19:00 hrs.");
        request.setFechaEnvio(LocalDateTime.of(2026, 7, 2, 10, 30, 0));

        NotificacionDTO dto = new NotificacionDTO();
        dto.setId(1L);
        dto.setDestinatario("juan.perez@colegio.cl");
        dto.setAsunto("Reunion de apoderados - ACTUALIZADA");
        dto.setMensaje("La reunion se ha reprogramado para el lunes 13 de julio a las 19:00 hrs.");
        dto.setFechaEnvio(LocalDateTime.of(2026, 7, 2, 10, 30, 0));

        when(service.actualizar(eq(1L), any(NotificacionRequest.class))).thenReturn(dto);

        mockMvc.perform(put("/api/v1/notificaciones/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.asunto").value("Reunion de apoderados - ACTUALIZADA"));
    }

    @Test
    @DisplayName("DELETE /api/v1/notificaciones/{id} - Eliminar notificacion retorna 204")
    void eliminar_retorna204() throws Exception {
        doNothing().when(service).eliminar(1L);

        mockMvc.perform(delete("/api/v1/notificaciones/1"))
                .andExpect(status().isNoContent());
    }
}
