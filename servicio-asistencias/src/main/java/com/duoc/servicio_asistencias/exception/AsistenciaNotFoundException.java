package com.duoc.servicio_asistencias.exception;

public class AsistenciaNotFoundException extends RuntimeException {
    public AsistenciaNotFoundException(Long id) {
        super("Asistencia no encontrado con id: " + id);
    }
}
