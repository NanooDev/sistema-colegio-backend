package com.duoc.servicio_matriculas.service;

import com.duoc.servicio_matriculas.client.CursoFeign;
import com.duoc.servicio_matriculas.client.EstudianteFeign;
import com.duoc.servicio_matriculas.dto.CursoDTO;
import com.duoc.servicio_matriculas.dto.EstudianteDTO;
import com.duoc.servicio_matriculas.dto.MatriculaDTO;
import com.duoc.servicio_matriculas.dto.MatriculaRequest;
import com.duoc.servicio_matriculas.exception.MatriculaNotFoundException;
import com.duoc.servicio_matriculas.model.Matricula;
import com.duoc.servicio_matriculas.repository.MatriculaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MatriculaService {

    private static final Logger log = LoggerFactory.getLogger(MatriculaService.class);

    @Autowired
    private MatriculaRepository repository;

    @Autowired
    private EstudianteFeign estudianteFeign;

    @Autowired
    private CursoFeign cursoFeign;

    public MatriculaDTO guardar(MatriculaRequest request) {
        Matricula e = new Matricula();
        e.setEstudianteId(request.getEstudianteId());
        e.setCursoId(request.getCursoId());
        e.setAnioEscolar(request.getAnioEscolar());
        return convertirADTO(repository.save(e));
    }

    public List<MatriculaDTO> listar() {
        return repository.findAll().stream().map(this::convertirADTO).collect(Collectors.toList());
    }

    public MatriculaDTO buscarPorId(Long id) {
        Matricula e = repository.findById(id).orElseThrow(() -> new MatriculaNotFoundException(id));
        return convertirADTO(e);
    }

    public MatriculaDTO actualizar(Long id, MatriculaRequest request) {
        Matricula ex = repository.findById(id).orElseThrow(() -> new MatriculaNotFoundException(id));
        ex.setEstudianteId(request.getEstudianteId());
        ex.setCursoId(request.getCursoId());
        ex.setAnioEscolar(request.getAnioEscolar());
        return convertirADTO(repository.save(ex));
    }

    public void eliminar(Long id) {
        repository.findById(id).orElseThrow(() -> new MatriculaNotFoundException(id));
        repository.deleteById(id);
    }

    public MatriculaDTO obtenerMatriculaConDetalles(Long id) {
        Matricula e = repository.findById(id).orElseThrow(() -> new MatriculaNotFoundException(id));
        MatriculaDTO dto = convertirADTO(e);

        try {
            EstudianteDTO estudiante = estudianteFeign.obtenerEstudiantePorId(e.getEstudianteId());
            dto.setNombreEstudiante(estudiante.getNombre() + " " + estudiante.getApellido());
        } catch (Exception ex) {
            log.error("No se pudo obtener el estudiante con ID {}: {}", e.getEstudianteId(), ex.getMessage());
        }

        try {
            CursoDTO curso = cursoFeign.obtenerCursoPorId(e.getCursoId());
            dto.setNombreCurso(curso.getNombre());
        } catch (Exception ex) {
            log.error("No se pudo obtener el curso con ID {}: {}", e.getCursoId(), ex.getMessage());
        }

        return dto;
    }

    private MatriculaDTO convertirADTO(Matricula e) {
        MatriculaDTO dto = new MatriculaDTO();
        dto.setCategoria("matricula");
        dto.setId(e.getId());
        dto.setEstudianteId(e.getEstudianteId());
        dto.setCursoId(e.getCursoId());
        dto.setAnioEscolar(e.getAnioEscolar());
        return dto;
    }
}
