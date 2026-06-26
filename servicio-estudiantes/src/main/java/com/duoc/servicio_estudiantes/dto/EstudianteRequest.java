package com.duoc.servicio_estudiantes.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class EstudianteRequest {

    @NotBlank(message = "El RUT es obligatorio")
    @Pattern(regexp = "^[0-9]+-[0-9kK]{1}$", message = "El RUT debe tener formato valido (12345678-9)")
    @Schema(description = "RUT del estudiante", example = "20123456-7")
    private String rut;

    @NotBlank(message = "El nombre es obligatorio")
    @Schema(description = "Nombre del estudiante", example = "Ana")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Schema(description = "Apellido del estudiante", example = "Pérez")
    private String apellido;

    @Schema(description = "Identificador del curso asignado", example = "1")
    private Long cursoId;
}
