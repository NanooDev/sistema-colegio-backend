package com.duoc.servicio_profesores.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/profesores")
public class ProfesorController {

    @GetMapping("/{id}")
    public Map<String, Object> obtenerProfesor(@PathVariable Long id) {
        // Datos simulados para la demostración
        Map<String, Object> profesor = new HashMap<>();
        profesor.put("id", id);
        profesor.put("nombre", "Alberto");
        profesor.put("apellido", "Einstein");
        profesor.put("especialidad", "Matemáticas Avanzadas");
        return profesor;
    }
}