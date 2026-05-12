package com.duoc.servicio_estudiantes.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "estudiantes")
@Data
public class Estudiante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private String rut;
    private String nombre;
    private String apellido;

    /** Curso al que pertenece (para listados por curso desde servicio-cursos). Opcional. */
    @Column(name = "curso_id")
    private Long cursoId;
}
