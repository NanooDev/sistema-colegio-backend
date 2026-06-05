package biblioteca.salas.duoc.biblioteca.salas.duoc.service;

import biblioteca.salas.duoc.biblioteca.salas.duoc.model.Sala;
import biblioteca.salas.duoc.biblioteca.salas.duoc.model.TipoSala;
import biblioteca.salas.duoc.biblioteca.salas.duoc.repository.SalaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SalaServiceTest {

    @Mock
    private SalaRepository salaRepository;

    @InjectMocks
    private SalaService salaService;

    @Test
    void findAll_deberiaRetornarTodasLasSalas() {
        TipoSala tipoSala = new TipoSala(1, "Lectura");
        Sala primera = new Sala(10, "Sala A", 20, 1, tipoSala);
        Sala segunda = new Sala(11, "Sala B", 30, 1, tipoSala);

        when(salaRepository.findAll()).thenReturn(List.of(primera, segunda));

        List<Sala> result = salaService.findAll();

        assertEquals(2, result.size());
        assertEquals(10, result.get(0).getCodigo());
        assertEquals(11, result.get(1).getCodigo());
    }

    @Test
    void findById_deberiaRetornarSalaCuandoExiste() {
        TipoSala tipoSala = new TipoSala(1, "Lectura");
        Sala sala = new Sala(10, "Sala A", 20, 1, tipoSala);

        when(salaRepository.findById(10)).thenReturn(Optional.of(sala));

        Sala result = salaService.findById(10);

        assertEquals(10, result.getCodigo());
        assertEquals("Sala A", result.getNombre());
    }

    @Test
    void findById_deberiaRetornarNullCuandoNoExiste() {
        when(salaRepository.findById(10)).thenReturn(Optional.empty());

        Sala result = salaService.findById(10);

        assertNull(result);
    }

    @Test
    void save_deberiaDelegarEnRepositorio() {
        TipoSala tipoSala = new TipoSala(1, "Lectura");
        Sala sala = new Sala(10, "Sala A", 20, 1, tipoSala);

        when(salaRepository.save(sala)).thenReturn(sala);

        Sala result = salaService.save(sala);

        assertEquals(10, result.getCodigo());
        verify(salaRepository).save(sala);
    }

    @Test
    void deleteById_deberiaDelegarEnRepositorio() {
        salaService.deleteById(10);

        verify(salaRepository).deleteById(10);
    }
}