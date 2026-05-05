package com.duoc.servicio_estudiantes.service;

import com.duoc.servicio_estudiantes.dto.EstudianteDTO;
import com.duoc.servicio_estudiantes.dto.EstudianteRequest;
import com.duoc.servicio_estudiantes.exception.EstudianteNotFoundException;
import com.duoc.servicio_estudiantes.model.Estudiante;
import com.duoc.servicio_estudiantes.repository.EstudianteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EstudianteService {

    @Autowired
    private EstudianteRepository estudianteRepository;

    public EstudianteDTO guardar(EstudianteRequest request) {
        Estudiante estudiante = new Estudiante();
        estudiante.setRut(request.getRut());
        estudiante.setNombre(request.getNombre());
        estudiante.setApellido(request.getApellido());

        return convertirADTO(estudianteRepository.save(estudiante));
    }

    public List<EstudianteDTO> listar() {
        return estudianteRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public EstudianteDTO buscarPorId(Integer id) {
        Estudiante estudiante = estudianteRepository.findById(id)
                .orElseThrow(() -> new EstudianteNotFoundException(id));
        return convertirADTO(estudiante);
    }

    public EstudianteDTO actualizar(Integer id, EstudianteRequest request) {
        Estudiante estudianteExistente = estudianteRepository.findById(id)
                .orElseThrow(() -> new EstudianteNotFoundException(id));

        estudianteExistente.setRut(request.getRut());
        estudianteExistente.setNombre(request.getNombre());
        estudianteExistente.setApellido(request.getApellido());

        return convertirADTO(estudianteRepository.save(estudianteExistente));
    }

    public void eliminar(Integer id) {
        estudianteRepository.findById(id).orElseThrow(() -> new EstudianteNotFoundException(id));
        estudianteRepository.deleteById(id);
    }

    private EstudianteDTO convertirADTO(Estudiante estudiante) {
        if (estudiante == null) return null;
        EstudianteDTO dto = new EstudianteDTO();
        dto.setId(estudiante.getId());
        dto.setRut(estudiante.getRut());
        dto.setNombre(estudiante.getNombre());
        dto.setApellido(estudiante.getApellido());
        return dto;
    }
}
