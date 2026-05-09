package com.duoc.servicio_calificaciones.exception;

public class CalificacionNotFoundException extends RuntimeException {
    public CalificacionNotFoundException(Integer id) {
        super("Calificación no encontrada con id: " + id);
    }
}