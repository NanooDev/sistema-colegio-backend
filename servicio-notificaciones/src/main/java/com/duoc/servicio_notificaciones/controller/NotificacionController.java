package com.duoc.servicio_notificaciones.controller;

import com.duoc.servicio_notificaciones.dto.NotificacionDTO;
import com.duoc.servicio_notificaciones.dto.NotificacionRequest;
import com.duoc.servicio_notificaciones.service.NotificacionService;
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
@RequestMapping("/api/v1/notificaciones")
@Tag(name = "Notificaciones", description = "Operaciones CRUD para notificaciones")
public class NotificacionController {

    @Autowired
    private NotificacionService service;

    @Operation(summary = "Crear una notificación", description = "Crea una nueva notificación en el sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Notificación creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<NotificacionDTO> guardar(@Valid @RequestBody NotificacionRequest request) {
        return new ResponseEntity<>(service.guardar(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Listar notificaciones", description = "Obtiene todas las notificaciones registradas")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente"),
        @ApiResponse(responseCode = "204", description = "No hay notificaciones registradas")
    })
    @GetMapping
    public ResponseEntity<List<NotificacionDTO>> listar() {
        List<NotificacionDTO> lista = service.listar();
        if (lista.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @Operation(summary = "Buscar notificación por ID", description = "Busca una notificación por su identificador")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Notificación encontrada"),
        @ApiResponse(responseCode = "404", description = "Notificación no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<NotificacionDTO> buscarPorId(@PathVariable Long id) {
        return new ResponseEntity<>(service.buscarPorId(id), HttpStatus.OK);
    }

    @Operation(summary = "Actualizar notificación", description = "Actualiza una notificación existente por su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Notificación actualizada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Notificación no encontrada"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<NotificacionDTO> actualizar(@PathVariable Long id, @Valid @RequestBody NotificacionRequest request) {
        return new ResponseEntity<>(service.actualizar(id, request), HttpStatus.OK);
    }

    @Operation(summary = "Eliminar notificación", description = "Elimina una notificación existente")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Notificación eliminada con exito"),
        @ApiResponse(responseCode = "404", description = "Notificación no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
