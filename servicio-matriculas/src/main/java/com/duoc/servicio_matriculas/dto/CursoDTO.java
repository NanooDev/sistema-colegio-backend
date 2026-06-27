package com.duoc.servicio_matriculas.dto;

import lombok.Data;

@Data
public class CursoDTO {
    private Long id;
    private String nombre;
    private Long profesorJefeId;
}
