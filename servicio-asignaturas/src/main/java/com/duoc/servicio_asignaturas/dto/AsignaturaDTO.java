package com.duoc.servicio_asignaturas.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@JsonPropertyOrder({ "categoria", "id", "nombre", "codigo" })
public class AsignaturaDTO {
    @Schema(description = "Categoría del recurso", example = "asignatura")
    private String categoria = "asignatura";

    @Schema(description = "Identificador único de la asignatura", example = "1")
    private Long id;

    @Schema(description = "Nombre de la asignatura", example = "Matemáticas")
    private String nombre;

    @Schema(description = "Código de la asignatura", example = "MAT-101")
    private String codigo;
}
