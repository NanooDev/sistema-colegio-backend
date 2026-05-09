package com.duoc.servicio_calificaciones.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalificacionRequest {

    @NotNull(message = "El ID del estudiante es obligatorio")
    private Integer estudianteId;

    @NotNull(message = "El ID del curso es obligatorio")
    private Integer cursoId;

    @NotNull(message = "La nota 1 es obligatoria")
    @DecimalMin(value = "1.0", message = "La nota 1 debe ser al menos 1.0")
    @DecimalMax(value = "7.0", message = "La nota 1 no puede ser mayor a 7.0")
    private Double nota1;

    @NotNull(message = "La nota 2 es obligatoria")
    @DecimalMin(value = "1.0", message = "La nota 2 debe ser al menos 1.0")
    @DecimalMax(value = "7.0", message = "La nota 2 no puede ser mayor a 7.0")
    private Double nota2;

    @NotNull(message = "La nota 3 es obligatoria")
    @DecimalMin(value = "1.0", message = "La nota 3 debe ser al menos 1.0")
    @DecimalMax(value = "7.0", message = "La nota 3 no puede ser mayor a 7.0")
    private Double nota3;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;
}