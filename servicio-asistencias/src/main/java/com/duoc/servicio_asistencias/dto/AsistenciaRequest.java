package com.duoc.servicio_asistencias.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class AsistenciaRequest {
    @NotNull(message = "El id del estudiante es obligatorio")
    private Long estudianteId;
    @NotNull(message = "El id del curso es obligatorio")
    private Long cursoId;
    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;
    @NotNull(message = "El campo presente es obligatorio")
    private Boolean presente;
}
