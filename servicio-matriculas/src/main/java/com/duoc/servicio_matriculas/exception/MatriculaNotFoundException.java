package com.duoc.servicio_matriculas.exception;

public class MatriculaNotFoundException extends RuntimeException {
    public MatriculaNotFoundException(Long id) {
        super("Matricula no encontrado con id: " + id);
    }
}
