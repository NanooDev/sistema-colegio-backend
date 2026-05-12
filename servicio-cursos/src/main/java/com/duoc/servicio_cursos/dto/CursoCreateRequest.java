package com.duoc.servicio_cursos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CursoCreateRequest {

    @NotBlank(message = "El nombre del curso es obligatorio")
    private String nombre;

    @NotNull(message = "El id del profesor jefe es obligatorio")
    private Long profesorJefeId;
}
