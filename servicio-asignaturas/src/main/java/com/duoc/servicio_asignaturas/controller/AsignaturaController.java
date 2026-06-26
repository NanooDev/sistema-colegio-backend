package com.duoc.servicio_asignaturas.controller;

import com.duoc.servicio_asignaturas.dto.AsignaturaDTO;
import com.duoc.servicio_asignaturas.dto.AsignaturaRequest;
import com.duoc.servicio_asignaturas.service.AsignaturaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/asignaturas")
@Tag(name = "Asignaturas", description = "Operaciones CRUD para asignaturas")
public class AsignaturaController {

    @Autowired
    private AsignaturaService service;

    @Operation(summary = "Crear una asignatura", description = "Crea una nueva asignatura en el sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Asignatura creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<AsignaturaDTO> guardar(@Valid @RequestBody AsignaturaRequest request) {
        return new ResponseEntity<>(service.guardar(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Listar asignaturas", description = "Obtiene todas las asignaturas registradas")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente"),
        @ApiResponse(responseCode = "204", description = "No hay asignaturas registradas")
    })
    @GetMapping
    public ResponseEntity<List<AsignaturaDTO>> listar() {
        List<AsignaturaDTO> lista = service.listar();
        if (lista.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @Operation(summary = "Buscar asignatura por ID", description = "Busca una asignatura por su identificador")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Asignatura encontrada"),
        @ApiResponse(responseCode = "404", description = "Asignatura no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<AsignaturaDTO> buscarPorId(@PathVariable Long id) {
        return new ResponseEntity<>(service.buscarPorId(id), HttpStatus.OK);
    }

    @Operation(summary = "Actualizar asignatura", description = "Actualiza una asignatura existente por su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Asignatura actualizada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Asignatura no encontrada"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<AsignaturaDTO> actualizar(@PathVariable Long id, @Valid @RequestBody AsignaturaRequest request) {
        return new ResponseEntity<>(service.actualizar(id, request), HttpStatus.OK);
    }

    @Operation(summary = "Eliminar asignatura", description = "Elimina una asignatura existente")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Asignatura eliminada con exito"),
        @ApiResponse(responseCode = "404", description = "Asignatura no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
