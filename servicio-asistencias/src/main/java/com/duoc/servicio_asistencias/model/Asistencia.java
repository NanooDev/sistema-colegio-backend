package com.duoc.servicio_asistencias.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "asistencias")
@Data
public class Asistencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "estudiante_id", nullable = false)
    private Long estudianteId;
    @Column(name = "curso_id", nullable = false)
    private Long cursoId;
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;
    @Column(name = "presente", nullable = false)
    private Boolean presente;
}
