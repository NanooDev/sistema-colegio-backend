package com.duoc.servicio_matriculas.client;

import com.duoc.servicio_matriculas.dto.CursoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "servicio-cursos", url = "${feign.cursos.url:http://localhost:8084/api/v1/cursos}")
public interface CursoFeign {

    @GetMapping("/{id}")
    CursoDTO obtenerCursoPorId(@PathVariable("id") Long id);
}
