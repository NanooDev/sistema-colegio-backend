package com.duoc.servicio_cursos.model;

import com.duoc.servicio_cursos.dto.EstudianteDTO;
import com.duoc.servicio_cursos.dto.ProfesorDTO;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.util.List;

@Data
@JsonPropertyOrder({ "categoria", "id", "nombre", "profesorJefeId", "profesorJefe", "estudiantes" })
public class Curso {

    private String categoria = "curso";
    private Long id;
    private String nombre;
    private Long profesorJefeId;
    private ProfesorDTO profesorJefe;
    private List<EstudianteDTO> estudiantes;
}