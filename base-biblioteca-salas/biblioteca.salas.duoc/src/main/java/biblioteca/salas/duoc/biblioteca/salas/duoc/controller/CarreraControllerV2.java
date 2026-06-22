package biblioteca.salas.duoc.biblioteca.salas.duoc.controller;

import biblioteca.salas.duoc.biblioteca.salas.duoc.assemblers.CarreraModelAssembler;
import biblioteca.salas.duoc.biblioteca.salas.duoc.model.Carrera;
import biblioteca.salas.duoc.biblioteca.salas.duoc.service.CarreraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/v2/carreras")
@Tag(name = "Carreras V2 (HATEOAS)", description = "Operaciones relacionadas con las carreras con hipermedia")
public class CarreraControllerV2 {

    @Autowired
    private CarreraService carreraService;

    @Autowired
    private CarreraModelAssembler assembler;

    @GetMapping
    @Operation(summary = "Obtener todas las carreras", description = "Devuelve una lista de todas las carreras disponibles con hipermedia")
    public CollectionModel<EntityModel<Carrera>> getAllCarreras() {
        List<EntityModel<Carrera>> carreras = carreraService.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(carreras,
                linkTo(methodOn(CarreraControllerV2.class).getAllCarreras()).withSelfRel());
    }

    @GetMapping("/{codigo}")
    @Operation(summary = "Obtener una carrera por código", description = "Devuelve una carrera específica usando su código con hipermedia")
    public EntityModel<Carrera> getCarreraByCodigo(@PathVariable String codigo) {
        Carrera carrera = carreraService.findByCodigo(codigo);
        return assembler.toModel(carrera);
    }
}
