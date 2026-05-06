package com.duoc.servicio_cursos.controller;

import com.duoc.servicio_cursos.model.Curso;
import com.duoc.servicio_cursos.service.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cursos")
public class CursoController {

    @Autowired
    private CursoService cursoService;

    @GetMapping("/{id}")
    public Curso obtenerCursoCompleto(@PathVariable Long id) {
        return cursoService.obtenerCursoConDetalles(id);
    }
}