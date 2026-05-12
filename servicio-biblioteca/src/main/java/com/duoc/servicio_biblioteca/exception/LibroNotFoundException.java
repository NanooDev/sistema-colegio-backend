package com.duoc.servicio_biblioteca.exception;

public class LibroNotFoundException extends RuntimeException {
    public LibroNotFoundException(Long id) {
        super("Libro no encontrado con id: " + id);
    }
}
