package com.duoc.servicio_estudiantes.controller;

import com.duoc.servicio_estudiantes.dto.EstudianteDTO;
import com.duoc.servicio_estudiantes.dto.EstudianteRequest;
import com.duoc.servicio_estudiantes.service.EstudianteService;
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
@RequestMapping("/api/v1/estudiantes")
@Tag(name = "Estudiantes", description = "Operaciones CRUD para estudiantes")
public class EstudianteController {

    @Autowired
    private EstudianteService estudianteService;

    @Operation(summary = "Crear un estudiante", description = "Crea un nuevo estudiante en el sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Estudiante creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<EstudianteDTO> guardar(@Valid @RequestBody EstudianteRequest request) {
        return new ResponseEntity<>(estudianteService.guardar(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Listar estudiantes", description = "Obtiene todos los estudiantes registrados")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente"),
        @ApiResponse(responseCode = "204", description = "No hay estudiantes registrados")
    })
    @GetMapping
    public ResponseEntity<List<EstudianteDTO>> listar() {
        List<EstudianteDTO> estudiantes = estudianteService.listar();
        if (estudiantes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(estudiantes, HttpStatus.OK);
    }

    @Operation(summary = "Buscar estudiante por ID", description = "Busca un estudiante por su identificador")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Estudiante encontrado"),
        @ApiResponse(responseCode = "404", description = "Estudiante no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EstudianteDTO> buscarPorId(@PathVariable Integer id) {
        return new ResponseEntity<>(estudianteService.buscarPorId(id), HttpStatus.OK);
    }

    @Operation(summary = "Buscar estudiantes por curso", description = "Obtiene todos los estudiantes asignados a un curso")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")
    })
    // Estudiantes asignados a un curso (campo curso_id en BD). Usado por servicio-cursos en Feign.
    @GetMapping("/curso/{cursoId}")
    public ResponseEntity<List<EstudianteDTO>> buscarPorCurso(@PathVariable Long cursoId) {
        List<EstudianteDTO> lista = estudianteService.listarPorCurso(cursoId);
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @Operation(summary = "Actualizar estudiante", description = "Actualiza un estudiante existente por su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Estudiante actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Estudiante no encontrado"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EstudianteDTO> actualizar(@PathVariable Integer id, @Valid @RequestBody EstudianteRequest request) {
        return new ResponseEntity<>(estudianteService.actualizar(id, request), HttpStatus.OK);
    }

    @Operation(summary = "Eliminar estudiante", description = "Elimina un estudiante existente")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Estudiante eliminado con exito"),
        @ApiResponse(responseCode = "404", description = "Estudiante no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        estudianteService.eliminar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
