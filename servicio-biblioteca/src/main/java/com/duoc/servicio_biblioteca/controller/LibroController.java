package com.duoc.servicio_biblioteca.controller;

import com.duoc.servicio_biblioteca.dto.LibroDTO;
import com.duoc.servicio_biblioteca.dto.LibroRequest;
import com.duoc.servicio_biblioteca.service.LibroService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/v1/biblioteca")
@Tag(name = "Biblioteca", description = "Operaciones CRUD para libros")
public class LibroController {

    @Autowired
    private LibroService service;

    @Operation(summary = "Crear un libro", description = "Crea un nuevo libro en el sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Libro creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<LibroDTO> guardar(@Valid @RequestBody LibroRequest request) {
        return new ResponseEntity<>(service.guardar(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Listar libros", description = "Obtiene todos los libros registrados")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente"),
        @ApiResponse(responseCode = "204", description = "No hay libros registrados")
    })
    @GetMapping
    public ResponseEntity<List<LibroDTO>> listar() {
        List<LibroDTO> lista = service.listar();
        if (lista.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @Operation(summary = "Buscar libro por ID", description = "Busca un libro por su identificador")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Libro encontrado"),
        @ApiResponse(responseCode = "404", description = "Libro no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<LibroDTO> buscarPorId(@PathVariable Long id) {
        return new ResponseEntity<>(service.buscarPorId(id), HttpStatus.OK);
    }

    @Operation(summary = "Actualizar libro", description = "Actualiza un libro existente por su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Libro actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Libro no encontrado"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<LibroDTO> actualizar(@PathVariable Long id, @Valid @RequestBody LibroRequest request) {
        return new ResponseEntity<>(service.actualizar(id, request), HttpStatus.OK);
    }

    @Operation(summary = "Eliminar libro", description = "Elimina un libro existente")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Libro eliminado con exito"),
        @ApiResponse(responseCode = "404", description = "Libro no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
