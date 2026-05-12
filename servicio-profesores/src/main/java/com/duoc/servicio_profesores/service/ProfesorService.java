package com.duoc.servicio_profesores.service;

import com.duoc.servicio_profesores.dto.ProfesorDTO;
import com.duoc.servicio_profesores.dto.ProfesorRequest;
import com.duoc.servicio_profesores.exception.ProfesorNotFoundException;
import com.duoc.servicio_profesores.model.Profesor;
import com.duoc.servicio_profesores.repository.ProfesorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfesorService {

    @Autowired
    private ProfesorRepository profesorRepository;

    public ProfesorDTO guardar(ProfesorRequest request) {
        Profesor profesor = new Profesor();
        profesor.setNombre(request.getNombre());
        profesor.setApellido(request.getApellido());
        profesor.setEspecialidad(request.getEspecialidad());
        return convertirADTO(profesorRepository.save(profesor));
    }

    public List<ProfesorDTO> listar() {
        return profesorRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public ProfesorDTO buscarPorId(Long id) {
        Profesor profesor = profesorRepository.findById(id)
                .orElseThrow(() -> new ProfesorNotFoundException(id));
        return convertirADTO(profesor);
    }

    public ProfesorDTO actualizar(Long id, ProfesorRequest request) {
        Profesor existente = profesorRepository.findById(id)
                .orElseThrow(() -> new ProfesorNotFoundException(id));
        existente.setNombre(request.getNombre());
        existente.setApellido(request.getApellido());
        existente.setEspecialidad(request.getEspecialidad());
        return convertirADTO(profesorRepository.save(existente));
    }

    public void eliminar(Long id) {
        profesorRepository.findById(id).orElseThrow(() -> new ProfesorNotFoundException(id));
        profesorRepository.deleteById(id);
    }

    private ProfesorDTO convertirADTO(Profesor profesor) {
        if (profesor == null) {
            return null;
        }
        ProfesorDTO dto = new ProfesorDTO();
        dto.setCategoria("profesor");
        dto.setId(profesor.getId());
        dto.setNombre(profesor.getNombre());
        dto.setApellido(profesor.getApellido());
        dto.setEspecialidad(profesor.getEspecialidad());
        return dto;
    }
}
