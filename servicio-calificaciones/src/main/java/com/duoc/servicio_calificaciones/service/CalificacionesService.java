package com.duoc.servicio_calificaciones.service;

import com.duoc.servicio_calificaciones.dto.CalificacionDTO;
import com.duoc.servicio_calificaciones.dto.CalificacionRequest;
import com.duoc.servicio_calificaciones.exception.CalificacionNotFoundException;
import com.duoc.servicio_calificaciones.model.Calificacion;
import com.duoc.servicio_calificaciones.repository.CalificacionesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CalificacionesService {

    @Autowired
    private CalificacionesRepository calificacionesRepository;

    public CalificacionDTO guardar(CalificacionRequest request) {
        Calificacion calificacion = new Calificacion();
        calificacion.setEstudianteId(request.getEstudianteId());
        calificacion.setCursoId(request.getCursoId());
        calificacion.setNota1(request.getNota1());
        calificacion.setNota2(request.getNota2());
        calificacion.setNota3(request.getNota3());
        calificacion.setNotaFinal(calcularNotaFinal(request.getNota1(), request.getNota2(), request.getNota3()));
        calificacion.setEstado(calcularEstado(calcularNotaFinal(request.getNota1(), request.getNota2(), request.getNota3())));
        calificacion.setFecha(request.getFecha());

        Calificacion saved = calificacionesRepository.save(calificacion);
        return convertirADTO(saved);
    }

    public List<CalificacionDTO> listar() {
        return calificacionesRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public CalificacionDTO buscarPorId(Integer id) {
        Calificacion calificacion = calificacionesRepository.findById(id)
                .orElseThrow(() -> new CalificacionNotFoundException(id));
        return convertirADTO(calificacion);
    }

    public List<CalificacionDTO> buscarPorEstudiante(Integer estudianteId) {
        return calificacionesRepository.findCalificacionesByEstudianteId(estudianteId).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public List<CalificacionDTO> buscarPorCurso(Integer cursoId) {
        return calificacionesRepository.findCalificacionesByCursoId(cursoId).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public CalificacionDTO actualizar(Integer id, CalificacionRequest request) {
        Calificacion calificacion = calificacionesRepository.findById(id)
                .orElseThrow(() -> new CalificacionNotFoundException(id));

        calificacion.setEstudianteId(request.getEstudianteId());
        calificacion.setCursoId(request.getCursoId());
        calificacion.setNota1(request.getNota1());
        calificacion.setNota2(request.getNota2());
        calificacion.setNota3(request.getNota3());
        calificacion.setNotaFinal(calcularNotaFinal(request.getNota1(), request.getNota2(), request.getNota3()));
        calificacion.setEstado(calcularEstado(calcularNotaFinal(request.getNota1(), request.getNota2(), request.getNota3())));
        calificacion.setFecha(request.getFecha());

        Calificacion updated = calificacionesRepository.save(calificacion);
        return convertirADTO(updated);
    }

    public void eliminar(Integer id) {
        if (!calificacionesRepository.existsById(id)) {
            throw new CalificacionNotFoundException(id);
        }
        calificacionesRepository.deleteById(id);
    }

    private Double calcularNotaFinal(Double nota1, Double nota2, Double nota3) {
        return (nota1 + nota2 + nota3) / 3.0;
    }

    private String calcularEstado(Double notaFinal) {
        return notaFinal >= 4.0 ? "Aprobado" : "Reprobado";
    }

    private CalificacionDTO convertirADTO(Calificacion calificacion) {
        return new CalificacionDTO(
                calificacion.getId(),
                calificacion.getEstudianteId(),
                calificacion.getCursoId(),
                calificacion.getNota1(),
                calificacion.getNota2(),
                calificacion.getNota3(),
                calificacion.getNotaFinal(),
                calificacion.getEstado(),
                calificacion.getFecha()
        );
    }
}