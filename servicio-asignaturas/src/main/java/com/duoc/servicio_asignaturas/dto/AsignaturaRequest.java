package com.duoc.servicio_asignaturas.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class AsignaturaRequest {
    @NotBlank(message = "El nombre es obligatorio")
    @Schema(description = "Nombre de la asignatura", example = "Matemáticas")
    private String nombre;

    @NotBlank(message = "El codigo es obligatorio")
    @Schema(description = "Código de la asignatura", example = "MAT-101")
    private String codigo;
}
