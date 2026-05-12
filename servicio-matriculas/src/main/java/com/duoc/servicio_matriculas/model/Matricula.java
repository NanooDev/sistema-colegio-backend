package com.duoc.servicio_matriculas.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "matriculas")
@Data
public class Matricula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "estudiante_id", nullable = false)
    private Long estudianteId;
    @Column(name = "curso_id", nullable = false)
    private Long cursoId;
    @Column(name = "anio_escolar", nullable = false)
    private String anioEscolar;
}
