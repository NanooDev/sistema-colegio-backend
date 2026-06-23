package com.duoc.servicio_biblioteca.controller;

import com.duoc.servicio_biblioteca.dto.LibroDTO;
import com.duoc.servicio_biblioteca.dto.LibroRequest;
import com.duoc.servicio_biblioteca.service.LibroService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/biblioteca")
public class LibroController {

    @Autowired
    private LibroService service;

    @PostMapping
    public ResponseEntity<LibroDTO> guardar(@Valid @RequestBody LibroRequest request) {
        return new ResponseEntity<>(service.guardar(request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<LibroDTO>> listar() {
        List<LibroDTO> lista = service.listar();
        if (lista.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LibroDTO> buscarPorId(@PathVariable Long id) {
        return new ResponseEntity<>(service.buscarPorId(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LibroDTO> actualizar(@PathVariable Long id, @Valid @RequestBody LibroRequest request) {
        return new ResponseEntity<>(service.actualizar(id, request), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
