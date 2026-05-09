package com.duoc.servicio_calificaciones.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "calificaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Calificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer estudianteId;

    @Column(nullable = false)
    private Integer cursoId;

    @Column(nullable = false)
    private Double nota1;

    @Column(nullable = false)
    private Double nota2;

    @Column(nullable = false)
    private Double nota3;

    @Column(nullable = false)
    private Double notaFinal;

    @Column(nullable = false)
    private String estado;

    @Column(nullable = false)
    private LocalDate fecha;
}