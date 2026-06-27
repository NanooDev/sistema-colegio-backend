package com.duoc.servicio_finanzas.dto;

import lombok.Data;

@Data
public class EstudianteDTO {
    private Long id;
    private String rut;
    private String nombre;
    private String apellido;
    private Long cursoId;
}
