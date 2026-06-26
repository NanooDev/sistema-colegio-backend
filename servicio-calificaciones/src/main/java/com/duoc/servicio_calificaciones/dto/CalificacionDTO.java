package com.duoc.servicio_calificaciones.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
@JsonPropertyOrder({ "categoria", "id", "estudianteId", "cursoId", "nota1", "nota2", "nota3", "notaFinal", "estado", "fecha" })
public class CalificacionDTO {

    @Schema(description = "Categoría del recurso", example = "calificacion")
    private String categoria = "calificacion";

    @Schema(description = "Identificador único de la calificación", example = "1")
    private Integer id;

    @Schema(description = "Identificador del estudiante", example = "1")
    private Integer estudianteId;

    @Schema(description = "Identificador del curso", example = "1")
    private Integer cursoId;

    @Schema(description = "Primera nota del estudiante", example = "6.0")
    private Double nota1;

    @Schema(description = "Segunda nota del estudiante", example = "5.5")
    private Double nota2;

    @Schema(description = "Tercera nota del estudiante", example = "7.0")
    private Double nota3;

    @Schema(description = "Nota final calculada", example = "6.2")
    private Double notaFinal;

    @Schema(description = "Estado de aprobación", example = "Aprobado")
    private String estado;

    @Schema(description = "Fecha de la calificación", example = "2023-01-15")
    private LocalDate fecha;
}
