package com.duoc.servicio_estudiantes.repository;

import com.duoc.servicio_estudiantes.model.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, Integer> {

    List<Estudiante> findByCursoId(Long cursoId);
    boolean existsByRut(String rut);
}
