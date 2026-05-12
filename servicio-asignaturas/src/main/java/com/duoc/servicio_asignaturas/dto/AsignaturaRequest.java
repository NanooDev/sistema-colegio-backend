package com.duoc.servicio_asignaturas.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class AsignaturaRequest {
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
    @NotBlank(message = "El codigo es obligatorio")
    private String codigo;
}
