package com.duoc.servicio_profesores.controller;

import com.duoc.servicio_profesores.dto.ProfesorDTO;
import com.duoc.servicio_profesores.dto.ProfesorRequest;
import com.duoc.servicio_profesores.service.ProfesorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/v1/profesores")
@Tag(name = "Profesores", description = "Operaciones CRUD para profesores")
public class ProfesorController {

    @Autowired
    private ProfesorService profesorService;

    @Operation(summary = "Crear un profesor", description = "Crea un nuevo profesor en el sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Profesor creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<ProfesorDTO> guardar(@Valid @RequestBody ProfesorRequest request) {
        return new ResponseEntity<>(profesorService.guardar(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Listar profesores", description = "Obtiene todos los profesores registrados")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente"),
        @ApiResponse(responseCode = "204", description = "No hay profesores registrados")
    })
    @GetMapping
    public ResponseEntity<List<ProfesorDTO>> listar() {
        List<ProfesorDTO> profesores = profesorService.listar();
        if (profesores.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(profesores, HttpStatus.OK);
    }

    @Operation(summary = "Buscar profesor por ID", description = "Busca un profesor por su identificador")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Profesor encontrado"),
        @ApiResponse(responseCode = "404", description = "Profesor no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProfesorDTO> buscarPorId(@PathVariable Long id) {
        return new ResponseEntity<>(profesorService.buscarPorId(id), HttpStatus.OK);
    }

    @Operation(summary = "Actualizar profesor", description = "Actualiza un profesor existente por su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Profesor actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Profesor no encontrado"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProfesorDTO> actualizar(@PathVariable Long id, @Valid @RequestBody ProfesorRequest request) {
        return new ResponseEntity<>(profesorService.actualizar(id, request), HttpStatus.OK);
    }

    @Operation(summary = "Eliminar profesor", description = "Elimina un profesor existente")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Profesor eliminado con exito"),
        @ApiResponse(responseCode = "404", description = "Profesor no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        profesorService.eliminar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
