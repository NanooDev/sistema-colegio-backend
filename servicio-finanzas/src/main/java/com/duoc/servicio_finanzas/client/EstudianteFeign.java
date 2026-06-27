package com.duoc.servicio_finanzas.client;

import com.duoc.servicio_finanzas.dto.EstudianteDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "servicio-estudiantes", url = "${feign.estudiantes.url:http://localhost:8081/api/v1/estudiantes}")
public interface EstudianteFeign {

    @GetMapping("/{id}")
    EstudianteDTO obtenerEstudiantePorId(@PathVariable("id") Long id);
}
