package com.duoc.servicio_finanzas.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;
import java.math.BigDecimal;

@Data
public class CargoRequest {
    private Long estudianteId;
    @NotBlank(message = "El concepto es obligatorio")
    private String concepto;
    @NotNull(message = "El monto es obligatorio")
    private BigDecimal monto;
    @NotNull(message = "La fecha de pago es obligatoria")
    private LocalDate fechaPago;
}
