package com.duoc.servicio_cursos.dto;

import lombok.Data;

@Data
public class ProfesorDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String especialidad;
}