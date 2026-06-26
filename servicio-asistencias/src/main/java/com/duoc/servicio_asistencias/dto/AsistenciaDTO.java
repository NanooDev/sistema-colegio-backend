package com.duoc.servicio_asistencias.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDate;

@Data
@JsonPropertyOrder({ "categoria", "id", "estudianteId", "cursoId", "fecha", "presente" })
public class AsistenciaDTO {
    @Schema(description = "Categoría del recurso", example = "asistencia")
    private String categoria = "asistencia";

    @Schema(description = "Identificador único de la asistencia", example = "1")
    private Long id;

    @Schema(description = "Identificador del estudiante", example = "1")
    private Long estudianteId;

    @Schema(description = "Identificador del curso", example = "1")
    private Long cursoId;

    @Schema(description = "Fecha de la asistencia", example = "2023-06-15")
    private LocalDate fecha;

    @Schema(description = "Indica si el estudiante estuvo presente", example = "true")
    private Boolean presente;
}
