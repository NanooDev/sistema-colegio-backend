package com.duoc.servicio_estudiantes.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@JsonPropertyOrder({ "categoria", "id", "nombre", "apellido", "rut", "cursoId" })
public class EstudianteDTO {

    @Schema(description = "Categoría del recurso", example = "estudiante")
    private String categoria = "estudiante";

    @Schema(description = "Identificador único del estudiante", example = "1")
    private Integer id;

    @Schema(description = "Nombre del estudiante", example = "Ana")
    private String nombre;

    @Schema(description = "Apellido del estudiante", example = "Pérez")
    private String apellido;

    @Schema(description = "RUT del estudiante", example = "20123456-7")
    private String rut;

    @Schema(description = "Identificador del curso asignado", example = "1")
    private Long cursoId;
}
