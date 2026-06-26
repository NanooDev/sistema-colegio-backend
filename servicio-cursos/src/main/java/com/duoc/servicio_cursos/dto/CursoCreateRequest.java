package com.duoc.servicio_cursos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CursoCreateRequest {

    @NotBlank(message = "El nombre del curso es obligatorio")
    @Schema(description = "Nombre del curso", example = "1A")
    private String nombre;

    @NotNull(message = "El id del profesor jefe es obligatorio")
    @Schema(description = "Identificador del profesor jefe", example = "1")
    private Long profesorJefeId;
}
