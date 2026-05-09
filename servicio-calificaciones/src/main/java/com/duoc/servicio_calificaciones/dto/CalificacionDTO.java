package com.duoc.servicio_calificaciones.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalificacionDTO {
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