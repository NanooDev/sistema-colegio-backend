package com.duoc.servicio_finanzas.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;
import java.math.BigDecimal;

@Data
public class CargoRequest {
    @Schema(description = "Identificador del estudiante", example = "1")
    private Long estudianteId;

    @NotBlank(message = "El concepto es obligatorio")
    @Schema(description = "Concepto del cargo", example = "Matrícula anual")
    private String concepto;

    @NotNull(message = "El monto es obligatorio")
    @Schema(description = "Monto del cargo", example = "150000")
    private BigDecimal monto;

    @NotNull(message = "La fecha de pago es obligatoria")
    @Schema(description = "Fecha de pago del cargo", example = "2023-03-01")
    private LocalDate fechaPago;
}
