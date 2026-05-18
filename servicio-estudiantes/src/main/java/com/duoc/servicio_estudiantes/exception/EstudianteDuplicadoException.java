package com.duoc.servicio_estudiantes.exception;

public class EstudianteDuplicadoException extends RuntimeException {
    public EstudianteDuplicadoException(String rut) {
        super("Ya existe un estudiante con el RUT: " + rut);
    }
}