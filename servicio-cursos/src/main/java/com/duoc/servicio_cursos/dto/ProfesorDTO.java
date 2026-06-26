package com.duoc.servicio_cursos.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@JsonPropertyOrder({ "categoria", "id", "nombre", "apellido", "especialidad" })
public class ProfesorDTO {

    @Schema(description = "Categoría del recurso", example = "profesor")
    private String categoria;

    @Schema(description = "Identificador único del profesor", example = "1")
    private Long id;

    @Schema(description = "Nombre del profesor", example = "Juan")
    private String nombre;

    @Schema(description = "Apellido del profesor", example = "García")
    private String apellido;

    @Schema(description = "Especialidad del profesor", example = "Matemáticas")
    private String especialidad;
}