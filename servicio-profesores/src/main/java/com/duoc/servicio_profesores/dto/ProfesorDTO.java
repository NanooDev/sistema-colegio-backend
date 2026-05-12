package com.duoc.servicio_profesores.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@Data
@JsonPropertyOrder({ "categoria", "id", "nombre", "apellido", "especialidad" })
public class ProfesorDTO {

    /** Siempre "profesor" para identificar el tipo de recurso en APIs y agregados (p. ej. curso). */
    private String categoria = "profesor";

    private Long id;
    private String nombre;
    private String apellido;
    private String especialidad;
}
