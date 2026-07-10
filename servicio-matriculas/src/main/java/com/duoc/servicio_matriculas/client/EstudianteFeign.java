package com.duoc.servicio_matriculas.client;

import com.duoc.servicio_matriculas.dto.EstudianteDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "servicio-estudiantes", path = "/api/v1/estudiantes")
public interface EstudianteFeign {

    @GetMapping("/{id}")
    EstudianteDTO obtenerEstudiantePorId(@PathVariable("id") Long id);
}
