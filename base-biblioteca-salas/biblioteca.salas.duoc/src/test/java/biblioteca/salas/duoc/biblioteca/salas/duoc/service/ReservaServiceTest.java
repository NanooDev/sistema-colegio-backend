package biblioteca.salas.duoc.biblioteca.salas.duoc.service;

import biblioteca.salas.duoc.biblioteca.salas.duoc.model.Carrera;
import biblioteca.salas.duoc.biblioteca.salas.duoc.model.Estudiante;
import biblioteca.salas.duoc.biblioteca.salas.duoc.model.Reserva;
import biblioteca.salas.duoc.biblioteca.salas.duoc.model.Sala;
import biblioteca.salas.duoc.biblioteca.salas.duoc.model.TipoSala;
import biblioteca.salas.duoc.biblioteca.salas.duoc.repository.ReservaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservaServiceTest {

    @Mock
    private ReservaRepository reservaRepository;

    @InjectMocks
    private ReservaService reservaService;

    @Test
    void findAll_deberiaRetornarTodasLasReservas() {
        Reserva primera = crearReserva(1);
        Reserva segunda = crearReserva(2);

        when(reservaRepository.findAll()).thenReturn(List.of(primera, segunda));

        List<Reserva> result = reservaService.findAll();

        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getId());
        assertEquals(2, result.get(1).getId());
    }

    @Test
    void findById_deberiaRetornarReservaCuandoExiste() {
        Reserva reserva = crearReserva(1);
        when(reservaRepository.findById(1)).thenReturn(Optional.of(reserva));

        Reserva result = reservaService.findById(1);

        assertEquals(1, result.getId());
        assertEquals(1, result.getEstado());
    }

    @Test
    void findById_deberiaRetornarNullCuandoNoExiste() {
        when(reservaRepository.findById(1)).thenReturn(Optional.empty());

        Reserva result = reservaService.findById(1);

        assertNull(result);
    }

    @SuppressWarnings("null")
    @Test
    void save_deberiaDelegarEnRepositorio() {
        Reserva reserva = crearReserva(1);
        when(reservaRepository.save(any(Reserva.class))).thenReturn(reserva);

        Reserva result = reservaService.save(reserva);

        assertEquals(1, result.getId());
        verify(reservaRepository).save(any(Reserva.class));
    }

    @Test
    void deleteById_deberiaDelegarEnRepositorio() {
        reservaService.deleteById(1);

        verify(reservaRepository).deleteById(1);
    }

    private Reserva crearReserva(int id) {
        Carrera carrera = new Carrera("INF", "Ingenieria Informatica");
        Estudiante estudiante = new Estudiante();
        estudiante.setId(1);
        estudiante.setNombres("Ana");
        estudiante.setCarrera(carrera);

        TipoSala tipoSala = new TipoSala(1, "Lectura");
        Sala sala = new Sala(10, "Sala A", 20, 1, tipoSala);

        Reserva reserva = new Reserva();
        reserva.setId(id);
        reserva.setFechaSolicitada(new Date());
        reserva.setHoraSolicitada(new Date());
        reserva.setHoraCierre(new Date());
        reserva.setEstado(1);
        reserva.setEstudiante(estudiante);
        reserva.setSala(sala);
        return reserva;
    }
}