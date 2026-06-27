package com.duoc.servicio_asistencias.controller;

import com.duoc.servicio_asistencias.dto.AsistenciaDTO;
import com.duoc.servicio_asistencias.dto.AsistenciaRequest;
import com.duoc.servicio_asistencias.service.AsistenciaService;
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
@RequestMapping("/api/v1/asistencias")
@Tag(name = "Asistencias", description = "Operaciones CRUD para asistencias")
public class AsistenciaController {

    @Autowired
    private AsistenciaService service;

    @Operation(summary = "Crear una asistencia", description = "Crea una nueva asistencia en el sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Asistencia creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<AsistenciaDTO> guardar(@Valid @RequestBody AsistenciaRequest request) {
        return new ResponseEntity<>(service.guardar(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Listar asistencias", description = "Obtiene todas las asistencias registradas")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente"),
        @ApiResponse(responseCode = "204", description = "No hay asistencias registradas")
    })
    @GetMapping
    public ResponseEntity<List<AsistenciaDTO>> listar() {
        List<AsistenciaDTO> lista = service.listar();
        if (lista.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @Operation(summary = "Buscar asistencia por ID", description = "Busca una asistencia por su identificador")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Asistencia encontrada"),
        @ApiResponse(responseCode = "404", description = "Asistencia no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<AsistenciaDTO> buscarPorId(@PathVariable Long id) {
        return new ResponseEntity<>(service.buscarPorId(id), HttpStatus.OK);
    }

    @Operation(summary = "Obtener asistencia con estudiante", description = "Obtiene una asistencia con el nombre del estudiante via comunicación REST")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Asistencia con detalles obtenida exitosamente"),
        @ApiResponse(responseCode = "404", description = "Asistencia no encontrada")
    })
    @GetMapping("/{id}/detalle")
    public ResponseEntity<AsistenciaDTO> obtenerConDetalles(@PathVariable Long id) {
        return new ResponseEntity<>(service.obtenerAsistenciaConEstudiante(id), HttpStatus.OK);
    }

    @Operation(summary = "Actualizar asistencia", description = "Actualiza una asistencia existente por su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Asistencia actualizada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Asistencia no encontrada"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<AsistenciaDTO> actualizar(@PathVariable Long id, @Valid @RequestBody AsistenciaRequest request) {
        return new ResponseEntity<>(service.actualizar(id, request), HttpStatus.OK);
    }

    @Operation(summary = "Eliminar asistencia", description = "Elimina una asistencia existente")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Asistencia eliminada con exito"),
        @ApiResponse(responseCode = "404", description = "Asistencia no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
