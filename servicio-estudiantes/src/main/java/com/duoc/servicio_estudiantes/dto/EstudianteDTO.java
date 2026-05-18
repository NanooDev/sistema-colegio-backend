package com.duoc.servicio_estudiantes.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@Data
@JsonPropertyOrder({ "categoria", "id", "nombre", "apellido", "rut", "cursoId" })
public class EstudianteDTO {

    //Siempre "estudiante" para identificar el tipo de recurso en APIs y agregados (ej curso).
    private String categoria = "estudiante";

    private Integer id;
    private String nombre;
    private String apellido;
    private String rut;
    private Long cursoId;
}
