package com.duoc.servicio_notificaciones.exception;

public class NotificacionNotFoundException extends RuntimeException {
    public NotificacionNotFoundException(Long id) {
        super("Notificacion no encontrado con id: " + id);
    }
}
