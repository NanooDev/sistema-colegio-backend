package com.duoc.servicio_asignaturas.exception;

public class AsignaturaNotFoundException extends RuntimeException {
    public AsignaturaNotFoundException(Long id) {
        super("Asignatura no encontrado con id: " + id);
    }
}
