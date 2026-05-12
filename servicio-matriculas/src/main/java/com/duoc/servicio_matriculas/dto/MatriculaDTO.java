package com.duoc.servicio_matriculas.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@Data
@JsonPropertyOrder({ "categoria", "id", "estudianteId", "cursoId", "anioEscolar" })
public class MatriculaDTO {
    private String categoria = "matricula";

    private Long id;
    private Long estudianteId;
    private Long cursoId;
    private String anioEscolar;
}
