package biblioteca.salas.duoc.biblioteca.salas.duoc.service;

import biblioteca.salas.duoc.biblioteca.salas.duoc.model.Carrera;
import biblioteca.salas.duoc.biblioteca.salas.duoc.repository.CarreraRepository;
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
class CarreraServiceTest {

    @Mock
    private CarreraRepository carreraRepository;

    @InjectMocks
    private CarreraService carreraService;

    @Test
    void findAll_deberiaRetornarTodasLasCarreras() {
        Carrera primera = new Carrera("INF", "Ingenieria Informatica");
        Carrera segunda = new Carrera("ADM", "Administracion");

        when(carreraRepository.findAll()).thenReturn(List.of(primera, segunda));

        List<Carrera> result = carreraService.findAll();

        assertEquals(2, result.size());
        assertEquals("INF", result.get(0).getCodigo());
        assertEquals("ADM", result.get(1).getCodigo());
    }

    @Test
    void findById_deberiaRetornarCarreraCuandoExiste() {
        Carrera carrera = new Carrera("INF", "Ingenieria Informatica");
        when(carreraRepository.findById("INF")).thenReturn(Optional.of(carrera));

        Carrera result = carreraService.findByCodigo("INF");

        assertEquals("INF", result.getCodigo());
        assertEquals("Ingenieria Informatica", result.getNombre());
    }

    @Test
    void findById_deberiaRetornarNullCuandoNoExiste() {
        when(carreraRepository.findById("INF")).thenReturn(Optional.empty());

        Carrera result = carreraService.findByCodigo("INF");

        assertNull(result);
    }

    @Test
    void save_deberiaDelegarEnRepositorio() {
        Carrera carrera = new Carrera("INF", "Ingenieria Informatica");
        when(carreraRepository.save(carrera)).thenReturn(carrera);

        Carrera result = carreraService.save(carrera);

        assertEquals("INF", result.getCodigo());
        verify(carreraRepository).save(carrera);
    }

    @Test
    void deleteById_deberiaDelegarEnRepositorio() {
        carreraService.deleteByCodigo("INF");

        verify(carreraRepository).deleteById("INF");
    }
}