package com.duoc.servicio_cursos.client;

import com.duoc.servicio_cursos.dto.ProfesorDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "servicio-profesores", url = "${feign.profesores.url:http://localhost:8082/api/profesores}")
public interface ProfesorFeign {
    @GetMapping("/{id}")
    ProfesorDTO obtenerProfesorPorId(@PathVariable("id") Long id);
}