package com.duoc.servicio_cursos.exception;

public class CursoNotFoundException extends RuntimeException {
    public CursoNotFoundException(Long id) {
        super("Curso no encontrado con id: " + id);
    }
}
