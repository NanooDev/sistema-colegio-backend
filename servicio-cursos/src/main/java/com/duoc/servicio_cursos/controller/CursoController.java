package com.duoc.servicio_cursos.controller;

import com.duoc.servicio_cursos.dto.CursoCreateRequest;
import com.duoc.servicio_cursos.model.Curso;
import com.duoc.servicio_cursos.service.CursoService;
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
@RequestMapping("/api/v1/cursos")
@Tag(name = "Cursos", description = "Operaciones CRUD para cursos")
public class CursoController {

    @Autowired
    private CursoService cursoService;

    @Operation(summary = "Listar cursos", description = "Obtiene todos los cursos registrados")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")
    })
    @GetMapping
    public ResponseEntity<List<Curso>> listar() {
        return ResponseEntity.ok(cursoService.listarCursos());
    }

    @Operation(summary = "Crear un curso", description = "Crea un nuevo curso en el sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Curso creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<Curso> crear(@Valid @RequestBody CursoCreateRequest request) {
        return new ResponseEntity<>(cursoService.crear(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar curso", description = "Actualiza un curso existente por su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Curso actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Curso no encontrado"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Curso> actualizar(@PathVariable Long id, @Valid @RequestBody CursoCreateRequest request) {
        return ResponseEntity.ok(cursoService.actualizar(id, request));
    }

    @Operation(summary = "Eliminar curso", description = "Elimina un curso existente")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Curso eliminado con exito"),
        @ApiResponse(responseCode = "404", description = "Curso no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        cursoService.eliminar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Obtener curso completo", description = "Obtiene un curso con todos sus detalles por su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Curso encontrado"),
        @ApiResponse(responseCode = "404", description = "Curso no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Curso> obtenerCursoCompleto(@PathVariable Long id) {
        return ResponseEntity.ok(cursoService.obtenerCursoConDetalles(id));
    }
}
