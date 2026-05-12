package com.duoc.servicio_notificaciones.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NotificacionRequest {
    @NotBlank(message = "El destinatario es obligatorio")
    private String destinatario;
    @NotBlank(message = "El asunto es obligatorio")
    private String asunto;
    @NotBlank(message = "El mensaje es obligatorio")
    private String mensaje;
    @NotNull(message = "La fecha de envio es obligatoria")
    private LocalDateTime fechaEnvio;
}
