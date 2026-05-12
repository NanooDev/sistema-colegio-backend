package com.duoc.servicio_biblioteca.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@Data
@JsonPropertyOrder({ "categoria", "id", "titulo", "autor", "ejemplaresDisponibles" })
public class LibroDTO {
    private String categoria = "libro";

    private Long id;
    private String titulo;
    private String autor;
    private Integer ejemplaresDisponibles;
}
