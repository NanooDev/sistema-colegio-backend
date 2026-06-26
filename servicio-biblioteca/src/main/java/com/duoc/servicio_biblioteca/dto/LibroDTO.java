package com.duoc.servicio_biblioteca.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@JsonPropertyOrder({ "categoria", "id", "titulo", "autor", "ejemplaresDisponibles" })
public class LibroDTO {
    @Schema(description = "Categoría del recurso", example = "libro")
    private String categoria = "libro";

    @Schema(description = "Identificador único del libro", example = "1")
    private Long id;

    @Schema(description = "Título del libro", example = "Don Quijote")
    private String titulo;

    @Schema(description = "Autor del libro", example = "Miguel de Cervantes")
    private String autor;

    @Schema(description = "Cantidad de ejemplares disponibles", example = "5")
    private Integer ejemplaresDisponibles;
}
