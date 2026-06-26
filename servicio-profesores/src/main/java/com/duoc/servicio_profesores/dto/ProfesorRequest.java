package com.duoc.servicio_profesores.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProfesorRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Schema(description = "Nombre del profesor", example = "Juan")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Schema(description = "Apellido del profesor", example = "García")
    private String apellido;

    @NotBlank(message = "La especialidad es obligatoria")
    @Schema(description = "Especialidad del profesor", example = "Matemáticas")
    private String especialidad;
}
