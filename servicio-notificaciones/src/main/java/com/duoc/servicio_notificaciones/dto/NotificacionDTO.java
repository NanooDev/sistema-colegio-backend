package com.duoc.servicio_notificaciones.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@JsonPropertyOrder({ "categoria", "id", "destinatario", "asunto", "mensaje", "fechaEnvio" })
public class NotificacionDTO {
    @Schema(description = "Categoría del recurso", example = "notificacion")
    private String categoria = "notificacion";

    @Schema(description = "Identificador único de la notificación", example = "1")
    private Long id;

    @Schema(description = "Correo electrónico del destinatario", example = "ana.perez@colegio.cl")
    private String destinatario;

    @Schema(description = "Asunto de la notificación", example = "Reunión de apoderados")
    private String asunto;

    @Schema(description = "Mensaje de la notificación", example = "Se convoca a reunión el día...")
    private String mensaje;

    @Schema(description = "Fecha y hora de envío de la notificación", example = "2023-06-15T10:30:00")
    private LocalDateTime fechaEnvio;
}
