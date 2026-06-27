package com.duoc.servicio_notificaciones.service;

import com.duoc.servicio_notificaciones.dto.NotificacionDTO;
import com.duoc.servicio_notificaciones.dto.NotificacionRequest;
import com.duoc.servicio_notificaciones.exception.NotificacionNotFoundException;
import com.duoc.servicio_notificaciones.model.Notificacion;
import com.duoc.servicio_notificaciones.repository.NotificacionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificacionServiceTest {

    @Mock
    private NotificacionRepository repository;

    @InjectMocks
    private NotificacionService notificacionService;

    // --- guardar ---

    @Test
    void guardar_conDatosValidos_deberiaCrearNotificacion() {
        // Given
        NotificacionRequest request = new NotificacionRequest();
        request.setDestinatario("apoderado@correo.com");
        request.setAsunto("Reunion de Apoderados");
        request.setMensaje("Se convoca a reunion el dia viernes");
        request.setFechaEnvio(LocalDateTime.of(2025, 6, 20, 10, 0));

        Notificacion guardada = new Notificacion();
        guardada.setId(1L);
        guardada.setDestinatario("apoderado@correo.com");
        guardada.setAsunto("Reunion de Apoderados");
        guardada.setMensaje("Se convoca a reunion el dia viernes");
        guardada.setFechaEnvio(LocalDateTime.of(2025, 6, 20, 10, 0));

        when(repository.save(any(Notificacion.class))).thenReturn(guardada);

        // When
        NotificacionDTO resultado = notificacionService.guardar(request);

        // Then
        assertNotNull(resultado);
        assertEquals("apoderado@correo.com", resultado.getDestinatario());
        assertEquals("Reunion de Apoderados", resultado.getAsunto());
        assertEquals("notificacion", resultado.getCategoria());
        verify(repository).save(any(Notificacion.class));
    }

    // --- listar ---

    @Test
    void listar_cuandoExistenNotificaciones_deberiaRetornarLista() {
        // Given
        Notificacion notif = new Notificacion();
        notif.setId(1L);
        notif.setDestinatario("profesor@correo.com");
        notif.setAsunto("Aviso");
        notif.setMensaje("Mensaje de prueba");
        notif.setFechaEnvio(LocalDateTime.now());

        when(repository.findAll()).thenReturn(List.of(notif));

        // When
        List<NotificacionDTO> resultado = notificacionService.listar();

        // Then
        assertEquals(1, resultado.size());
        assertEquals("profesor@correo.com", resultado.get(0).getDestinatario());
    }

    @Test
    void listar_cuandoNoExistenNotificaciones_deberiaRetornarListaVacia() {
        // Given
        when(repository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<NotificacionDTO> resultado = notificacionService.listar();

        // Then
        assertTrue(resultado.isEmpty());
    }

    // --- buscarPorId ---

    @Test
    void buscarPorId_cuandoExiste_deberiaRetornarDTO() {
        // Given
        Notificacion notif = new Notificacion();
        notif.setId(5L);
        notif.setDestinatario("admin@colegio.cl");
        notif.setAsunto("Informe Mensual");
        notif.setMensaje("Adjunto informe");
        notif.setFechaEnvio(LocalDateTime.of(2025, 5, 1, 8, 30));

        when(repository.findById(5L)).thenReturn(Optional.of(notif));

        // When
        NotificacionDTO resultado = notificacionService.buscarPorId(5L);

        // Then
        assertNotNull(resultado);
        assertEquals(5L, resultado.getId());
        assertEquals("admin@colegio.cl", resultado.getDestinatario());
    }

    @Test
    void buscarPorId_cuandoNoExiste_deberiaLanzarExcepcion() {
        // Given
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(NotificacionNotFoundException.class, () -> notificacionService.buscarPorId(99L));
    }

    // --- actualizar ---

    @Test
    void actualizar_cuandoExiste_deberiaActualizarYRetornarDTO() {
        // Given
        Notificacion existente = new Notificacion();
        existente.setId(1L);
        existente.setDestinatario("viejo@correo.com");
        existente.setAsunto("Asunto viejo");
        existente.setMensaje("Mensaje viejo");
        existente.setFechaEnvio(LocalDateTime.of(2025, 6, 1, 9, 0));

        NotificacionRequest request = new NotificacionRequest();
        request.setDestinatario("nuevo@correo.com");
        request.setAsunto("Asunto actualizado");
        request.setMensaje("Mensaje actualizado");
        request.setFechaEnvio(LocalDateTime.of(2025, 6, 25, 14, 0));

        Notificacion actualizada = new Notificacion();
        actualizada.setId(1L);
        actualizada.setDestinatario("nuevo@correo.com");
        actualizada.setAsunto("Asunto actualizado");
        actualizada.setMensaje("Mensaje actualizado");
        actualizada.setFechaEnvio(LocalDateTime.of(2025, 6, 25, 14, 0));

        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(any(Notificacion.class))).thenReturn(actualizada);

        // When
        NotificacionDTO resultado = notificacionService.actualizar(1L, request);

        // Then
        assertEquals("nuevo@correo.com", resultado.getDestinatario());
        assertEquals("Asunto actualizado", resultado.getAsunto());
    }

    @Test
    void actualizar_cuandoNoExiste_deberiaLanzarExcepcion() {
        // Given
        NotificacionRequest request = new NotificacionRequest();
        request.setDestinatario("test@test.com");
        request.setAsunto("Test");
        request.setMensaje("Test");
        request.setFechaEnvio(LocalDateTime.now());

        when(repository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(NotificacionNotFoundException.class, () -> notificacionService.actualizar(99L, request));
        verify(repository, never()).save(any());
    }

    // --- eliminar ---

    @Test
    void eliminar_cuandoExiste_deberiaEliminar() {
        // Given
        Notificacion notif = new Notificacion();
        notif.setId(10L);

        when(repository.findById(10L)).thenReturn(Optional.of(notif));
        doNothing().when(repository).deleteById(10L);

        // When
        notificacionService.eliminar(10L);

        // Then
        verify(repository).deleteById(10L);
    }

    @Test
    void eliminar_cuandoNoExiste_deberiaLanzarExcepcion() {
        // Given
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(NotificacionNotFoundException.class, () -> notificacionService.eliminar(99L));
        verify(repository, never()).deleteById(any());
    }
}
