package com.duoc.servicio_calificaciones.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.time.LocalDate;

@Data
@JsonPropertyOrder({ "categoria", "id", "estudianteId", "cursoId", "nota1", "nota2", "nota3", "notaFinal", "estado", "fecha" })
public class CalificacionDTO {

    private String categoria = "calificacion";

    private Integer id;
    private Integer estudianteId;
    private Integer cursoId;
    private Double nota1;
    private Double nota2;
    private Double nota3;
    private Double notaFinal;
    private String estado;
    private LocalDate fecha;
}
