package com.duoc.servicio_cursos.client;

import com.duoc.servicio_cursos.dto.ProfesorDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "servicio-profesores", path = "/api/v1/profesores")
public interface ProfesorFeign {
    @GetMapping("/{id}")
    ProfesorDTO obtenerProfesorPorId(@PathVariable("id") Long id);
}