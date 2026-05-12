package com.duoc.servicio_finanzas.exception;

public class CargoNotFoundException extends RuntimeException {
    public CargoNotFoundException(Long id) {
        super("Cargo no encontrado con id: " + id);
    }
}
