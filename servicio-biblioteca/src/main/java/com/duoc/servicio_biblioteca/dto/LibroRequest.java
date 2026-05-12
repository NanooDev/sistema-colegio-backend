package com.duoc.servicio_biblioteca.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class LibroRequest {
    @NotBlank(message = "El titulo es obligatorio")
    private String titulo;
    @NotBlank(message = "El autor es obligatorio")
    private String autor;
    @NotNull(message = "Los ejemplares disponibles son obligatorios")
    private Integer ejemplaresDisponibles;
}
