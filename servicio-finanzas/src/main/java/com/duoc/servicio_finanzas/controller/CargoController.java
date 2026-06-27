package com.duoc.servicio_finanzas.controller;

import com.duoc.servicio_finanzas.dto.CargoDTO;
import com.duoc.servicio_finanzas.dto.CargoRequest;
import com.duoc.servicio_finanzas.service.CargoService;
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
@RequestMapping("/api/v1/finanzas")
@Tag(name = "Finanzas", description = "Operaciones CRUD para cargos financieros")
public class CargoController {

    @Autowired
    private CargoService service;

    @Operation(summary = "Crear un cargo", description = "Crea un nuevo cargo financiero en el sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Cargo creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<CargoDTO> guardar(@Valid @RequestBody CargoRequest request) {
        return new ResponseEntity<>(service.guardar(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Listar cargos", description = "Obtiene todos los cargos financieros registrados")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente"),
        @ApiResponse(responseCode = "204", description = "No hay cargos registrados")
    })
    @GetMapping
    public ResponseEntity<List<CargoDTO>> listar() {
        List<CargoDTO> lista = service.listar();
        if (lista.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @Operation(summary = "Buscar cargo por ID", description = "Busca un cargo financiero por su identificador")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cargo encontrado"),
        @ApiResponse(responseCode = "404", description = "Cargo no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CargoDTO> buscarPorId(@PathVariable Long id) {
        return new ResponseEntity<>(service.buscarPorId(id), HttpStatus.OK);
    }

    @Operation(summary = "Obtener cargo con estudiante", description = "Obtiene un cargo con el nombre del estudiante via comunicación REST")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cargo con detalles obtenido exitosamente"),
        @ApiResponse(responseCode = "404", description = "Cargo no encontrado")
    })
    @GetMapping("/{id}/detalle")
    public ResponseEntity<CargoDTO> obtenerConDetalles(@PathVariable Long id) {
        return new ResponseEntity<>(service.obtenerCargoConEstudiante(id), HttpStatus.OK);
    }

    @Operation(summary = "Actualizar cargo", description = "Actualiza un cargo financiero existente por su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cargo actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Cargo no encontrado"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CargoDTO> actualizar(@PathVariable Long id, @Valid @RequestBody CargoRequest request) {
        return new ResponseEntity<>(service.actualizar(id, request), HttpStatus.OK);
    }

    @Operation(summary = "Eliminar cargo", description = "Elimina un cargo financiero existente")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Cargo eliminado con exito"),
        @ApiResponse(responseCode = "404", description = "Cargo no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
