package com.duoc.servicio_calificaciones.service;

import com.duoc.servicio_calificaciones.client.EstudianteFeign;
import com.duoc.servicio_calificaciones.dto.CalificacionDTO;
import com.duoc.servicio_calificaciones.dto.CalificacionRequest;
import com.duoc.servicio_calificaciones.dto.EstudianteDTO;
import com.duoc.servicio_calificaciones.exception.CalificacionNotFoundException;
import com.duoc.servicio_calificaciones.model.Calificacion;
import com.duoc.servicio_calificaciones.repository.CalificacionesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CalificacionesService {

    private static final Logger log = LoggerFactory.getLogger(CalificacionesService.class);

    @Autowired
    private CalificacionesRepository calificacionesRepository;

    @Autowired
    private EstudianteFeign estudianteFeign;

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

    public CalificacionDTO obtenerCalificacionConEstudiante(Integer id) {
        Calificacion calificacion = calificacionesRepository.findById(id)
                .orElseThrow(() -> new CalificacionNotFoundException(id));
        CalificacionDTO dto = convertirADTO(calificacion);

        try {
            EstudianteDTO estudiante = estudianteFeign.obtenerEstudiantePorId(calificacion.getEstudianteId());
            dto.setNombreEstudiante(estudiante.getNombre() + " " + estudiante.getApellido());
        } catch (Exception ex) {
            log.error("No se pudo obtener el estudiante con ID {}: {}", calificacion.getEstudianteId(), ex.getMessage());
        }

        return dto;
    }

    private Double calcularNotaFinal(Double nota1, Double nota2, Double nota3) {
        return (nota1 + nota2 + nota3) / 3.0;
    }

    private String calcularEstado(Double notaFinal) {
        return notaFinal >= 4.0 ? "Aprobado" : "Reprobado";
    }

    private CalificacionDTO convertirADTO(Calificacion calificacion) {
        CalificacionDTO dto = new CalificacionDTO();
        dto.setCategoria("calificacion");
        dto.setId(calificacion.getId());
        dto.setEstudianteId(calificacion.getEstudianteId());
        dto.setCursoId(calificacion.getCursoId());
        dto.setNota1(calificacion.getNota1());
        dto.setNota2(calificacion.getNota2());
        dto.setNota3(calificacion.getNota3());
        dto.setNotaFinal(calificacion.getNotaFinal());
        dto.setEstado(calificacion.getEstado());
        dto.setFecha(calificacion.getFecha());
        return dto;
    }
}