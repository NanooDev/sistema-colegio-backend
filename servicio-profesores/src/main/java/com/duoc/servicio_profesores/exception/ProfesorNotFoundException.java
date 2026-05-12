package com.duoc.servicio_profesores.exception;

public class ProfesorNotFoundException extends RuntimeException {
    public ProfesorNotFoundException(Long id) {
        super("Profesor no encontrado con id: " + id);
    }
}
