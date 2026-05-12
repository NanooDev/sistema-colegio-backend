package com.duoc.servicio_profesores.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProfesorRequest {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;

    @NotBlank(message = "La especialidad es obligatoria")
    private String especialidad;
}
