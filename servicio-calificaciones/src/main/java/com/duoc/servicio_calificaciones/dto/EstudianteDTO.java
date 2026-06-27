package com.duoc.servicio_calificaciones.dto;

import lombok.Data;

@Data
public class EstudianteDTO {
    private Integer id;
    private String rut;
    private String nombre;
    private String apellido;
    private Long cursoId;
}
