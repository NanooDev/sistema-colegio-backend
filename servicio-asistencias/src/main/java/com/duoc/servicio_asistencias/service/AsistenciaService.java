package com.duoc.servicio_asistencias.service;

import com.duoc.servicio_asistencias.client.EstudianteFeign;
import com.duoc.servicio_asistencias.dto.AsistenciaDTO;
import com.duoc.servicio_asistencias.dto.AsistenciaRequest;
import com.duoc.servicio_asistencias.dto.EstudianteDTO;
import com.duoc.servicio_asistencias.exception.AsistenciaNotFoundException;
import com.duoc.servicio_asistencias.model.Asistencia;
import com.duoc.servicio_asistencias.repository.AsistenciaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AsistenciaService {

    private static final Logger log = LoggerFactory.getLogger(AsistenciaService.class);

    @Autowired
    private AsistenciaRepository repository;

    @Autowired
    private EstudianteFeign estudianteFeign;

    public AsistenciaDTO guardar(AsistenciaRequest request) {
        Asistencia e = new Asistencia();
        e.setEstudianteId(request.getEstudianteId());
        e.setCursoId(request.getCursoId());
        e.setFecha(request.getFecha());
        e.setPresente(request.getPresente());
        return convertirADTO(repository.save(e));
    }

    public List<AsistenciaDTO> listar() {
        return repository.findAll().stream().map(this::convertirADTO).collect(Collectors.toList());
    }

    public AsistenciaDTO buscarPorId(Long id) {
        Asistencia e = repository.findById(id).orElseThrow(() -> new AsistenciaNotFoundException(id));
        return convertirADTO(e);
    }

    public AsistenciaDTO actualizar(Long id, AsistenciaRequest request) {
        Asistencia ex = repository.findById(id).orElseThrow(() -> new AsistenciaNotFoundException(id));
        ex.setEstudianteId(request.getEstudianteId());
        ex.setCursoId(request.getCursoId());
        ex.setFecha(request.getFecha());
        ex.setPresente(request.getPresente());
        return convertirADTO(repository.save(ex));
    }

    public void eliminar(Long id) {
        repository.findById(id).orElseThrow(() -> new AsistenciaNotFoundException(id));
        repository.deleteById(id);
    }

    public AsistenciaDTO obtenerAsistenciaConEstudiante(Long id) {
        Asistencia e = repository.findById(id).orElseThrow(() -> new AsistenciaNotFoundException(id));
        AsistenciaDTO dto = convertirADTO(e);

        try {
            EstudianteDTO estudiante = estudianteFeign.obtenerEstudiantePorId(e.getEstudianteId());
            dto.setNombreEstudiante(estudiante.getNombre() + " " + estudiante.getApellido());
        } catch (Exception ex) {
            log.error("No se pudo obtener el estudiante con ID {}: {}", e.getEstudianteId(), ex.getMessage());
        }

        return dto;
    }

    private AsistenciaDTO convertirADTO(Asistencia e) {
        AsistenciaDTO dto = new AsistenciaDTO();
        dto.setCategoria("asistencia");
        dto.setId(e.getId());
        dto.setEstudianteId(e.getEstudianteId());
        dto.setCursoId(e.getCursoId());
        dto.setFecha(e.getFecha());
        dto.setPresente(e.getPresente());
        return dto;
    }
}
