package com.duoc.servicio_asistencias.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class AsistenciaRequest {
    @NotNull(message = "El id del estudiante es obligatorio")
    @Schema(description = "Identificador del estudiante", example = "1")
    private Long estudianteId;

    @NotNull(message = "El id del curso es obligatorio")
    @Schema(description = "Identificador del curso", example = "1")
    private Long cursoId;

    @NotNull(message = "La fecha es obligatoria")
    @Schema(description = "Fecha de la asistencia", example = "2023-06-15")
    private LocalDate fecha;

    @NotNull(message = "El campo presente es obligatorio")
    @Schema(description = "Indica si el estudiante estuvo presente", example = "true")
    private Boolean presente;
}
