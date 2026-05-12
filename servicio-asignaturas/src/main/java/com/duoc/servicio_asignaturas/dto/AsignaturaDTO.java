package com.duoc.servicio_asignaturas.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@Data
@JsonPropertyOrder({ "categoria", "id", "nombre", "codigo" })
public class AsignaturaDTO {
    private String categoria = "asignatura";

    private Long id;
    private String nombre;
    private String codigo;
}
