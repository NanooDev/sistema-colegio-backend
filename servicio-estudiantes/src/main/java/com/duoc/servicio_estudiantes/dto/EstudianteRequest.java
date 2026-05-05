package com.duoc.servicio_estudiantes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class EstudianteRequest {

    @NotBlank(message = "El RUT es obligatorio")
    @Pattern(regexp = "^[0-9]+-[0-9kK]{1}$", message = "El RUT debe tener formato valido (e.g., 12345678-9)")
    private String rut;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;
}
