package com.duoc.servicio_notificaciones.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@JsonPropertyOrder({ "categoria", "id", "destinatario", "asunto", "mensaje", "fechaEnvio" })
public class NotificacionDTO {
    private String categoria = "notificacion";

    private Long id;
    private String destinatario;
    private String asunto;
    private String mensaje;
    private LocalDateTime fechaEnvio;
}
