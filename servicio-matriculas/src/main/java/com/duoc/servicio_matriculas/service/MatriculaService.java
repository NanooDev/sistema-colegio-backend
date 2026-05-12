package com.duoc.servicio_matriculas.service;

import com.duoc.servicio_matriculas.dto.MatriculaDTO;
import com.duoc.servicio_matriculas.dto.MatriculaRequest;
import com.duoc.servicio_matriculas.exception.MatriculaNotFoundException;
import com.duoc.servicio_matriculas.model.Matricula;
import com.duoc.servicio_matriculas.repository.MatriculaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MatriculaService {

    @Autowired
    private MatriculaRepository repository;

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
