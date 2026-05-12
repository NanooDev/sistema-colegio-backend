package com.duoc.servicio_profesores.controller;

import com.duoc.servicio_profesores.model.Profesor;
import com.duoc.servicio_profesores.service.ProfesorService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@RestController
@RequestMapping("/api/profesores")
public class ProfesorController {

    @Autowired
    private ProfesorService profesorService;

    @GetMapping("/{id}")
    public Optional<Profesor> obtenerProfesor(@PathVariable Long id) {
        return profesorService.obtenerProfesorPorId(id);
    }
}