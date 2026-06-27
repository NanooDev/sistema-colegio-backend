package com.duoc.servicio_matriculas.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@JsonPropertyOrder({ "categoria", "id", "estudianteId", "nombreEstudiante", "cursoId", "nombreCurso", "anioEscolar" })
public class MatriculaDTO {
    @Schema(description = "Categoría del recurso", example = "matricula")
    private String categoria = "matricula";

    @Schema(description = "Identificador único de la matrícula", example = "1")
    private Long id;

    @Schema(description = "Identificador del estudiante", example = "1")
    private Long estudianteId;

    @Schema(description = "Nombre completo del estudiante (obtenido via comunicación REST)", example = "Ana Perez")
    private String nombreEstudiante;

    @Schema(description = "Identificador del curso", example = "1")
    private Long cursoId;

    @Schema(description = "Nombre del curso (obtenido via comunicación REST)", example = "1ro Basico A")
    private String nombreCurso;

    @Schema(description = "Año escolar de la matrícula", example = "2023")
    private String anioEscolar;
}
