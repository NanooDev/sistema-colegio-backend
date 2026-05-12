package com.duoc.servicio_finanzas.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import java.time.LocalDate;
import java.math.BigDecimal;

@Data
@JsonPropertyOrder({ "categoria", "id", "estudianteId", "concepto", "monto", "fechaPago" })
public class CargoDTO {
    private String categoria = "cargo";

    private Long id;
    private Long estudianteId;
    private String concepto;
    private BigDecimal monto;
    private LocalDate fechaPago;
}
