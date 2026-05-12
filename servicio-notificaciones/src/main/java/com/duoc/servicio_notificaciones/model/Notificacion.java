package com.duoc.servicio_notificaciones.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "notificaciones")
@Data
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "destinatario", nullable = false)
    private String destinatario;
    @Column(name = "asunto", nullable = false)
    private String asunto;
    @Column(name = "mensaje", nullable = false)
    private String mensaje;
    @Column(name = "fecha_envio", nullable = false)
    private LocalDateTime fechaEnvio;
}
