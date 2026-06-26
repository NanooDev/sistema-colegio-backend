package com.duoc.servicio_matriculas.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class MatriculaRequest {
    @NotNull(message = "El id del estudiante es obligatorio")
    @Schema(description = "Identificador del estudiante", example = "1")
    private Long estudianteId;

    @NotNull(message = "El id del curso es obligatorio")
    @Schema(description = "Identificador del curso", example = "1")
    private Long cursoId;

    @NotBlank(message = "El año escolar es obligatorio")
    @Schema(description = "Año escolar de la matrícula", example = "2023")
    private String anioEscolar;
}
