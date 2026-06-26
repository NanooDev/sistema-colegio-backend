package com.duoc.servicio_matriculas.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@JsonPropertyOrder({ "categoria", "id", "estudianteId", "cursoId", "anioEscolar" })
public class MatriculaDTO {
    @Schema(description = "Categoría del recurso", example = "matricula")
    private String categoria = "matricula";

    @Schema(description = "Identificador único de la matrícula", example = "1")
    private Long id;

    @Schema(description = "Identificador del estudiante", example = "1")
    private Long estudianteId;

    @Schema(description = "Identificador del curso", example = "1")
    private Long cursoId;

    @Schema(description = "Año escolar de la matrícula", example = "2023")
    private String anioEscolar;
}
