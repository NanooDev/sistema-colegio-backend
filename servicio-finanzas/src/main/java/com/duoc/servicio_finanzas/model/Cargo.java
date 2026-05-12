package com.duoc.servicio_finanzas.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.math.BigDecimal;

@Entity
@Table(name = "cargos")
@Data
public class Cargo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "estudiante_id")
    private Long estudianteId;
    @Column(name = "concepto", nullable = false)
    private String concepto;
    @Column(name = "monto", nullable = false)
    private BigDecimal monto;
    @Column(name = "fecha_pago", nullable = false)
    private LocalDate fechaPago;
}
