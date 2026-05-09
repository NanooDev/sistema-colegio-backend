package com.duoc.servicio_calificaciones.controller;

import com.duoc.servicio_calificaciones.dto.CalificacionDTO;
import com.duoc.servicio_calificaciones.dto.CalificacionRequest;
import com.duoc.servicio_calificaciones.service.CalificacionesService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/calificaciones")
public class CalificacionesController {

    @Autowired
    private CalificacionesService calificacionesService;

    @PostMapping
    public ResponseEntity<CalificacionDTO> guardar(@Valid @RequestBody CalificacionRequest request) {
        return new ResponseEntity<>(calificacionesService.guardar(request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CalificacionDTO>> listar() {
        List<CalificacionDTO> calificaciones = calificacionesService.listar();
        if (calificaciones.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(calificaciones, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CalificacionDTO> buscarPorId(@PathVariable Integer id) {
        return new ResponseEntity<>(calificacionesService.buscarPorId(id), HttpStatus.OK);
    }

    @GetMapping("/estudiante/{estudianteId}")
    public ResponseEntity<List<CalificacionDTO>> buscarPorEstudiante(@PathVariable Integer estudianteId) {
        List<CalificacionDTO> calificaciones = calificacionesService.buscarPorEstudiante(estudianteId);
        if (calificaciones.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(calificaciones, HttpStatus.OK);
    }

    @GetMapping("/curso/{cursoId}")
    public ResponseEntity<List<CalificacionDTO>> buscarPorCurso(@PathVariable Integer cursoId) {
        List<CalificacionDTO> calificaciones = calificacionesService.buscarPorCurso(cursoId);
        if (calificaciones.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(calificaciones, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CalificacionDTO> actualizar(@PathVariable Integer id, @Valid @RequestBody CalificacionRequest request) {
        return new ResponseEntity<>(calificacionesService.actualizar(id, request), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        calificacionesService.eliminar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}