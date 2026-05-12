package com.duoc.servicio_profesores.service;

import com.duoc.servicio_profesores.model.Profesor;
import com.duoc.servicio_profesores.repository.ProfesorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfesorService {

    @Autowired
    private ProfesorRepository profesorRepository;

    public Optional<Profesor> obtenerProfesorPorId(Long id) {
        return profesorRepository.findById(id);
    }
}