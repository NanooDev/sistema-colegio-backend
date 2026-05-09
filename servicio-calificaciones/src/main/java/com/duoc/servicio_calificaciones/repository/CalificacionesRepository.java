package com.duoc.servicio_calificaciones.repository;

import com.duoc.servicio_calificaciones.model.Calificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CalificacionesRepository extends JpaRepository<Calificacion, Integer> {

    List<Calificacion> findCalificacionesByEstudianteId(Integer estudianteId);

    List<Calificacion> findCalificacionesByCursoId(Integer cursoId);
}