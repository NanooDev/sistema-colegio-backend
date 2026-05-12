package com.duoc.servicio_cursos.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@Data
@JsonPropertyOrder({ "categoria", "id", "nombre", "apellido", "rut", "cursoId" })
public class EstudianteDTO {

    private String categoria;
    private Long id;
    private String nombre;
    private String apellido;
    private String rut;
    private Long cursoId;
}