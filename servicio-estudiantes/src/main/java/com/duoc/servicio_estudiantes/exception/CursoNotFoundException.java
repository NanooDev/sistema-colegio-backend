package com.duoc.servicio_estudiantes.exception;

public class CursoNotFoundException extends RuntimeException {
    public CursoNotFoundException(Long cursoId) {
        super("Curso no encontrado con id: " + cursoId);
    }
}