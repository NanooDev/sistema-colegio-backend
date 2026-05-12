package com.duoc.servicio_cursos.model;

import com.duoc.servicio_cursos.dto.EstudianteDTO;
import com.duoc.servicio_cursos.dto.ProfesorDTO;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.util.List;

@Data
@JsonPropertyOrder({ "categoria", "id", "nombre", "profesorJefeId", "profesorJefe", "estudiantes" })
public class Curso {

    /** Siempre "curso" para identificar el agregado en el front u otros servicios. */
    private String categoria = "curso";
    private Long id;
    private String nombre;
    private Long profesorJefeId; // ID del profesor
    private ProfesorDTO profesorJefe; // Datos completos del profesor traídos de su servicio
    private List<EstudianteDTO> estudiantes; // Estudiantes traídos de su servicio
}