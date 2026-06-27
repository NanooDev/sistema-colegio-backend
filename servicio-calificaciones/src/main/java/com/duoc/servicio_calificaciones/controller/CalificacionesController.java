package com.duoc.servicio_calificaciones.controller;

import com.duoc.servicio_calificaciones.dto.CalificacionDTO;
import com.duoc.servicio_calificaciones.dto.CalificacionRequest;
import com.duoc.servicio_calificaciones.service.CalificacionesService;
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
@RequestMapping("/api/v1/calificaciones")
@Tag(name = "Calificaciones", description = "Operaciones CRUD para calificaciones")
public class CalificacionesController {

    @Autowired
    private CalificacionesService calificacionesService;

    @Operation(summary = "Crear una calificación", description = "Crea una nueva calificación en el sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Calificación creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<CalificacionDTO> guardar(@Valid @RequestBody CalificacionRequest request) {
        return new ResponseEntity<>(calificacionesService.guardar(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Listar calificaciones", description = "Obtiene todas las calificaciones registradas")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente"),
        @ApiResponse(responseCode = "204", description = "No hay calificaciones registradas")
    })
    @GetMapping
    public ResponseEntity<List<CalificacionDTO>> listar() {
        List<CalificacionDTO> calificaciones = calificacionesService.listar();
        if (calificaciones.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(calificaciones, HttpStatus.OK);
    }

    @Operation(summary = "Buscar calificación por ID", description = "Busca una calificación por su identificador")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Calificación encontrada"),
        @ApiResponse(responseCode = "404", description = "Calificación no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CalificacionDTO> buscarPorId(@PathVariable Integer id) {
        return new ResponseEntity<>(calificacionesService.buscarPorId(id), HttpStatus.OK);
    }

    @Operation(summary = "Obtener calificación con estudiante", description = "Obtiene una calificación con el nombre del estudiante via comunicación REST")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Calificación con detalles obtenida exitosamente"),
        @ApiResponse(responseCode = "404", description = "Calificación no encontrada")
    })
    @GetMapping("/{id}/detalle")
    public ResponseEntity<CalificacionDTO> obtenerConDetalles(@PathVariable Integer id) {
        return new ResponseEntity<>(calificacionesService.obtenerCalificacionConEstudiante(id), HttpStatus.OK);
    }

    @Operation(summary = "Buscar calificaciones por estudiante", description = "Obtiene todas las calificaciones de un estudiante")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente"),
        @ApiResponse(responseCode = "204", description = "No hay calificaciones para el estudiante")
    })
    @GetMapping("/estudiante/{estudianteId}")
    public ResponseEntity<List<CalificacionDTO>> buscarPorEstudiante(@PathVariable Integer estudianteId) {
        List<CalificacionDTO> calificaciones = calificacionesService.buscarPorEstudiante(estudianteId);
        if (calificaciones.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(calificaciones, HttpStatus.OK);
    }

    @Operation(summary = "Buscar calificaciones por curso", description = "Obtiene todas las calificaciones de un curso")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente"),
        @ApiResponse(responseCode = "204", description = "No hay calificaciones para el curso")
    })
    @GetMapping("/curso/{cursoId}")
    public ResponseEntity<List<CalificacionDTO>> buscarPorCurso(@PathVariable Integer cursoId) {
        List<CalificacionDTO> calificaciones = calificacionesService.buscarPorCurso(cursoId);
        if (calificaciones.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(calificaciones, HttpStatus.OK);
    }

    @Operation(summary = "Actualizar calificación", description = "Actualiza una calificación existente por su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Calificación actualizada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Calificación no encontrada"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CalificacionDTO> actualizar(@PathVariable Integer id, @Valid @RequestBody CalificacionRequest request) {
        return new ResponseEntity<>(calificacionesService.actualizar(id, request), HttpStatus.OK);
    }

    @Operation(summary = "Eliminar calificación", description = "Elimina una calificación existente")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Calificación eliminada con exito"),
        @ApiResponse(responseCode = "404", description = "Calificación no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        calificacionesService.eliminar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}