package com.duoc.servicio_biblioteca.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class LibroRequest {
    @NotBlank(message = "El titulo es obligatorio")
    @Schema(description = "Título del libro", example = "Don Quijote")
    private String titulo;

    @NotBlank(message = "El autor es obligatorio")
    @Schema(description = "Autor del libro", example = "Miguel de Cervantes")
    private String autor;

    @NotNull(message = "Los ejemplares disponibles son obligatorios")
    @Schema(description = "Cantidad de ejemplares disponibles", example = "5")
    private Integer ejemplaresDisponibles;
}
