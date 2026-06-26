package com.duoc.servicio_notificaciones.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NotificacionRequest {
    @NotBlank(message = "El destinatario es obligatorio")
    @Schema(description = "Correo electrónico del destinatario", example = "ana.perez@colegio.cl")
    private String destinatario;

    @NotBlank(message = "El asunto es obligatorio")
    @Schema(description = "Asunto de la notificación", example = "Reunión de apoderados")
    private String asunto;

    @NotBlank(message = "El mensaje es obligatorio")
    @Schema(description = "Mensaje de la notificación", example = "Se convoca a reunión el día...")
    private String mensaje;

    @NotNull(message = "La fecha de envio es obligatoria")
    @Schema(description = "Fecha y hora de envío de la notificación", example = "2023-06-15T10:30:00")
    private LocalDateTime fechaEnvio;
}
