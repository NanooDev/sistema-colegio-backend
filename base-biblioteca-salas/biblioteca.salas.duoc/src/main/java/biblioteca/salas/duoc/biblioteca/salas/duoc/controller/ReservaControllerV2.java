package biblioteca.salas.duoc.biblioteca.salas.duoc.controller;

import biblioteca.salas.duoc.biblioteca.salas.duoc.model.Reserva;
import biblioteca.salas.duoc.biblioteca.salas.duoc.service.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
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
@RequestMapping("/api/v2/reservas")
@Tag(name = "Reservas V2 (HATEOAS)", description = "Operaciones relacionadas con las reservas con hipermedia")
public class ReservaControllerV2 {

    @Autowired
    private ReservaService reservaService;

    @GetMapping(value = "/sala/{codigoSala}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener reservas por sala", description = "Devuelve todas las reservas de una sala específica con hipermedia")
    public CollectionModel<EntityModel<Reserva>> getReservasBySala(@PathVariable Integer codigoSala) {
        List<EntityModel<Reserva>> reservas = reservaService.findBySalaCodigo(codigoSala).stream()
                .map(reserva -> EntityModel.of(reserva,
                        linkTo(methodOn(ReservaControllerV2.class).getReservasBySala(codigoSala)).withSelfRel()))
                .collect(Collectors.toList());

        return CollectionModel.of(reservas,
                linkTo(methodOn(ReservaControllerV2.class).getReservasBySala(codigoSala)).withSelfRel());
    }
}
