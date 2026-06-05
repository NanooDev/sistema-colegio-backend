package biblioteca.salas.duoc.biblioteca.salas.duoc.service;

import biblioteca.salas.duoc.biblioteca.salas.duoc.model.Estudiante;
import biblioteca.salas.duoc.biblioteca.salas.duoc.repository.EstudianteRepository;
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
class EstudianteServiceTest {

    @Mock
    private EstudianteRepository estudianteRepository;

    @InjectMocks
    private EstudianteService estudianteService;

    @Test
    void findAll_deberiaRetornarTodosLosEstudiantes() {
        Estudiante primero = new Estudiante();
        primero.setId(1);
        primero.setNombres("Ana");

        Estudiante segundo = new Estudiante();
        segundo.setId(2);
        segundo.setNombres("Luis");

        when(estudianteRepository.findAll()).thenReturn(List.of(primero, segundo));

        List<Estudiante> result = estudianteService.findAll();

        assertEquals(2, result.size());
        assertEquals("Ana", result.get(0).getNombres());
        assertEquals("Luis", result.get(1).getNombres());
    }

    @Test
    void findById_deberiaRetornarEntidadCuandoExiste() {
        Estudiante estudiante = new Estudiante();
        estudiante.setId(10);
        estudiante.setNombres("María");

        when(estudianteRepository.findById(10)).thenReturn(Optional.of(estudiante));

        Estudiante result = estudianteService.findById(10);

        assertEquals(10, result.getId());
        assertEquals("María", result.getNombres());
    }

    @Test
    void findById_deberiaRetornarNullCuandoNoExiste() {
        when(estudianteRepository.findById(99)).thenReturn(Optional.empty());

        Estudiante result = estudianteService.findById(99);

        assertNull(result);
    }

    @Test
    void save_deberiaDelegarEnRepositorio() {
        Estudiante estudiante = new Estudiante();
        estudiante.setId(20);
        estudiante.setNombres("Pedro");

        when(estudianteRepository.save(estudiante)).thenReturn(estudiante);

        Estudiante result = estudianteService.save(estudiante);

        assertEquals(20, result.getId());
        verify(estudianteRepository).save(estudiante);
    }

    @Test
    void deleteById_deberiaDelegarEnRepositorio() {
        estudianteService.deleteById(7);

        verify(estudianteRepository).deleteById(7);
    }
}