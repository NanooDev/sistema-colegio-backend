package com.duoc.servicio_estudiantes.exception;

public class EstudianteNotFoundException extends RuntimeException {
    public EstudianteNotFoundException(Integer id) {
        super("Estudiante no encontrado con id: " + id);
    }
}
