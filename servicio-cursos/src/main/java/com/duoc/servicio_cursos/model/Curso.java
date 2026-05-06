package com.duoc.servicio_cursos.model;

import lombok.Data;
import java.util.List;
import com.duoc.servicio_cursos.dto.ProfesorDTO;
import com.duoc.servicio_cursos.dto.EstudianteDTO;

@Data
public class Curso {
    private Long id;
    private String nombre;
    private Long profesorJefeId; // ID del profesor
    private ProfesorDTO profesorJefe; // Datos completos del profesor traídos de su servicio
    private List<EstudianteDTO> estudiantes; // Estudiantes traídos de su servicio
}