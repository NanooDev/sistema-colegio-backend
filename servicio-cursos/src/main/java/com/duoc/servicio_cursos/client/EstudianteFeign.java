package com.duoc.servicio_cursos.client;

import com.duoc.servicio_cursos.dto.EstudianteDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

@FeignClient(name = "servicio-estudiantes", url = "http://localhost:8081/api/v1/estudiantes")
public interface EstudianteFeign {
    @GetMapping("/curso/{cursoId}")
    List<EstudianteDTO> obtenerEstudiantesPorCurso(@PathVariable("cursoId") Long cursoId);
}