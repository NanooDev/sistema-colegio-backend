package com.duoc.servicio_estudiantes.controller;

import com.duoc.servicio_estudiantes.dto.EstudianteDTO;
import com.duoc.servicio_estudiantes.dto.EstudianteRequest;
import com.duoc.servicio_estudiantes.service.EstudianteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/estudiantes")
public class EstudianteController {

    @Autowired
    private EstudianteService estudianteService;

    @PostMapping
    public ResponseEntity<EstudianteDTO> guardar(@Valid @RequestBody EstudianteRequest request) {
        return new ResponseEntity<>(estudianteService.guardar(request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<EstudianteDTO>> listar() {
        List<EstudianteDTO> estudiantes = estudianteService.listar();
        if (estudiantes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(estudiantes, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstudianteDTO> buscarPorId(@PathVariable Integer id) {
        return new ResponseEntity<>(estudianteService.buscarPorId(id), HttpStatus.OK);
    }

    // Endpoint simulado para interactuar con servicio-cursos
    @GetMapping("/curso/{cursoId}")
    public ResponseEntity<List<EstudianteDTO>> buscarPorCurso(@PathVariable Long cursoId) {
        EstudianteDTO e1 = new EstudianteDTO();
        e1.setId(1);
        e1.setNombre("Juan");
        e1.setApellido("Perez");
        e1.setRut("11.111.111-1");

        EstudianteDTO e2 = new EstudianteDTO();
        e2.setId(2);
        e2.setNombre("Maria");
        e2.setApellido("Gomez");
        e2.setRut("22.222.222-2");

        return new ResponseEntity<>(List.of(e1, e2), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstudianteDTO> actualizar(@PathVariable Integer id, @Valid @RequestBody EstudianteRequest request) {
        return new ResponseEntity<>(estudianteService.actualizar(id, request), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        estudianteService.eliminar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
