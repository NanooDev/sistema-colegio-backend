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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificacionServiceTest {

    @Mock
    private NotificacionRepository repository;

    @InjectMocks
    private NotificacionService notificacionService;

    @Test
    void guardar_deberiaCrearYRetornarDTO() {
        NotificacionRequest request = new NotificacionRequest();
        request.setDestinatario("ana@correo.cl");
        request.setAsunto("Aviso");
        request.setMensaje("Hola");
        request.setFechaEnvio(LocalDateTime.of(2026, 6, 3, 10, 0));

        Notificacion saved = new Notificacion();
        saved.setId(1L);
        saved.setDestinatario("ana@correo.cl");
        saved.setAsunto("Aviso");
        saved.setMensaje("Hola");
        saved.setFechaEnvio(LocalDateTime.of(2026, 6, 3, 10, 0));

        when(repository.save(any(Notificacion.class))).thenReturn(saved);

        NotificacionDTO result = notificacionService.guardar(request);

        assertEquals(1L, result.getId());
        assertEquals("notificacion", result.getCategoria());
    }

    @Test
    void listar_deberiaRetornarTodasLasNotificaciones() {
        Notificacion n = new Notificacion();
        n.setId(1L);
        when(repository.findAll()).thenReturn(List.of(n));

        List<NotificacionDTO> result = notificacionService.listar();

        assertEquals(1, result.size());
    }

    @Test
    void buscarPorId_deberiaRetornarDTOCuandoExiste() {
        Notificacion n = new Notificacion();
        n.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(n));

        NotificacionDTO result = notificacionService.buscarPorId(1L);

        assertEquals(1L, result.getId());
    }

    @Test
    void buscarPorId_deberiaLanzarExcepcionCuandoNoExiste() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotificacionNotFoundException.class, () -> notificacionService.buscarPorId(1L));
    }

    @Test
    void actualizar_deberiaActualizarYRetornarDTO() {
        Notificacion existente = new Notificacion();
        existente.setId(1L);
        NotificacionRequest request = new NotificacionRequest();
        request.setDestinatario("ana@correo.cl");
        request.setAsunto("Nuevo");
        request.setMensaje("Nuevo mensaje");
        request.setFechaEnvio(LocalDateTime.of(2026, 6, 4, 11, 0));

        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(existente)).thenReturn(existente);

        NotificacionDTO result = notificacionService.actualizar(1L, request);

        assertEquals("Nuevo", result.getAsunto());
        verify(repository).save(existente);
    }

    @Test
    void eliminar_deberiaBorrarSiExiste() {
        Notificacion existente = new Notificacion();
        existente.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(existente));

        notificacionService.eliminar(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void eliminar_deberiaLanzarExcepcionSiNoExiste() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotificacionNotFoundException.class, () -> notificacionService.eliminar(1L));
        verify(repository, never()).deleteById(any());
    }
}