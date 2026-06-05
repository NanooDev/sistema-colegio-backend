package biblioteca.salas.duoc.biblioteca.salas.duoc.service;

import biblioteca.salas.duoc.biblioteca.salas.duoc.model.TipoSala;
import biblioteca.salas.duoc.biblioteca.salas.duoc.repository.TipoSalaRepository;
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
class TipoSalaServiceTest {

    @Mock
    private TipoSalaRepository tipoSalaRepository;

    @InjectMocks
    private TipoSalaService tipoSalaService;

    @Test
    void findAll_deberiaRetornarTodosLosTipos() {
        TipoSala primero = new TipoSala(1, "Lectura");
        TipoSala segundo = new TipoSala(2, "Reunion");

        when(tipoSalaRepository.findAll()).thenReturn(List.of(primero, segundo));

        List<TipoSala> result = tipoSalaService.findAll();

        assertEquals(2, result.size());
        assertEquals("Lectura", result.get(0).getNombre());
        assertEquals("Reunion", result.get(1).getNombre());
    }

    @Test
    void findById_deberiaRetornarTipoCuandoExiste() {
        TipoSala tipoSala = new TipoSala(1, "Lectura");

        when(tipoSalaRepository.findById(1)).thenReturn(Optional.of(tipoSala));

        TipoSala result = tipoSalaService.findById(1);

        assertEquals(1, result.getIdTipo());
        assertEquals("Lectura", result.getNombre());
    }

    @Test
    void findById_deberiaRetornarNullCuandoNoExiste() {
        when(tipoSalaRepository.findById(1)).thenReturn(Optional.empty());

        TipoSala result = tipoSalaService.findById(1);

        assertNull(result);
    }

    @Test
    void save_deberiaDelegarEnRepositorio() {
        TipoSala tipoSala = new TipoSala(1, "Lectura");
        when(tipoSalaRepository.save(tipoSala)).thenReturn(tipoSala);

        TipoSala result = tipoSalaService.save(tipoSala);

        assertEquals(1, result.getIdTipo());
        verify(tipoSalaRepository).save(tipoSala);
    }

    @Test
    void deleteById_deberiaDelegarEnRepositorio() {
        tipoSalaService.deleteById(1);

        verify(tipoSalaRepository).deleteById(1);
    }
}