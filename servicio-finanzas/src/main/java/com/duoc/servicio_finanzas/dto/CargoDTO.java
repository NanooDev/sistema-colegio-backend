package com.duoc.servicio_finanzas.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDate;
import java.math.BigDecimal;

@Data
@JsonPropertyOrder({ "categoria", "id", "estudianteId", "concepto", "monto", "fechaPago" })
public class CargoDTO {
    @Schema(description = "Categoría del recurso", example = "cargo")
    private String categoria = "cargo";

    @Schema(description = "Identificador único del cargo", example = "1")
    private Long id;

    @Schema(description = "Identificador del estudiante", example = "1")
    private Long estudianteId;

    @Schema(description = "Concepto del cargo", example = "Matrícula anual")
    private String concepto;

    @Schema(description = "Monto del cargo", example = "150000")
    private BigDecimal monto;

    @Schema(description = "Fecha de pago del cargo", example = "2023-03-01")
    private LocalDate fechaPago;
}
