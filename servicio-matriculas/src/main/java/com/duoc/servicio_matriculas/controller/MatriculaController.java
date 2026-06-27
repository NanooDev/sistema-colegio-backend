package com.duoc.servicio_matriculas.controller;

import com.duoc.servicio_matriculas.dto.MatriculaDTO;
import com.duoc.servicio_matriculas.dto.MatriculaRequest;
import com.duoc.servicio_matriculas.service.MatriculaService;
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
@RequestMapping("/api/v1/matriculas")
@Tag(name = "Matrículas", description = "Operaciones CRUD para matrículas")
public class MatriculaController {

    @Autowired
    private MatriculaService service;

    @Operation(summary = "Crear una matrícula", description = "Crea una nueva matrícula en el sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Matrícula creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<MatriculaDTO> guardar(@Valid @RequestBody MatriculaRequest request) {
        return new ResponseEntity<>(service.guardar(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Listar matrículas", description = "Obtiene todas las matrículas registradas")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente"),
        @ApiResponse(responseCode = "204", description = "No hay matrículas registradas")
    })
    @GetMapping
    public ResponseEntity<List<MatriculaDTO>> listar() {
        List<MatriculaDTO> lista = service.listar();
        if (lista.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @Operation(summary = "Buscar matrícula por ID", description = "Busca una matrícula por su identificador")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Matrícula encontrada"),
        @ApiResponse(responseCode = "404", description = "Matrícula no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<MatriculaDTO> buscarPorId(@PathVariable Long id) {
        return new ResponseEntity<>(service.buscarPorId(id), HttpStatus.OK);
    }

    @Operation(summary = "Obtener matrícula con detalles", description = "Obtiene una matrícula con información del estudiante y curso via comunicación REST")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Matrícula con detalles obtenida exitosamente"),
        @ApiResponse(responseCode = "404", description = "Matrícula no encontrada")
    })
    @GetMapping("/{id}/detalle")
    public ResponseEntity<MatriculaDTO> obtenerConDetalles(@PathVariable Long id) {
        return new ResponseEntity<>(service.obtenerMatriculaConDetalles(id), HttpStatus.OK);
    }

    @Operation(summary = "Actualizar matrícula", description = "Actualiza una matrícula existente por su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Matrícula actualizada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Matrícula no encontrada"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<MatriculaDTO> actualizar(@PathVariable Long id, @Valid @RequestBody MatriculaRequest request) {
        return new ResponseEntity<>(service.actualizar(id, request), HttpStatus.OK);
    }

    @Operation(summary = "Eliminar matrícula", description = "Elimina una matrícula existente")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Matrícula eliminada con exito"),
        @ApiResponse(responseCode = "404", description = "Matrícula no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
