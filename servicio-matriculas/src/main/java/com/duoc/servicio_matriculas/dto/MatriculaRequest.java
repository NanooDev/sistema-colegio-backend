package com.duoc.servicio_matriculas.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class MatriculaRequest {
    @NotNull(message = "El id del estudiante es obligatorio")
    private Long estudianteId;
    @NotNull(message = "El id del curso es obligatorio")
    private Long cursoId;
    @NotBlank(message = "El año escolar es obligatorio")
    private String anioEscolar;
}
