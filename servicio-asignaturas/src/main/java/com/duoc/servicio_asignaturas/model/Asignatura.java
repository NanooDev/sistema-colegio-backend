package com.duoc.servicio_asignaturas.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "asignaturas")
@Data
public class Asignatura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false)
    private String nombre;
    @Column(name = "codigo", nullable = false)
    private String codigo;
}
