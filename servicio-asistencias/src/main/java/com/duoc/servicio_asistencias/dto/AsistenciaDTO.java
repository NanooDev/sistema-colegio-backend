package com.duoc.servicio_asistencias.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import java.time.LocalDate;

@Data
@JsonPropertyOrder({ "categoria", "id", "estudianteId", "cursoId", "fecha", "presente" })
public class AsistenciaDTO {
    private String categoria = "asistencia";

    private Long id;
    private Long estudianteId;
    private Long cursoId;
    private LocalDate fecha;
    private Boolean presente;
}
