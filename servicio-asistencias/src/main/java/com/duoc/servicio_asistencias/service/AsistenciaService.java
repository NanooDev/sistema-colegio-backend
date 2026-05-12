package com.duoc.servicio_asistencias.service;

import com.duoc.servicio_asistencias.dto.AsistenciaDTO;
import com.duoc.servicio_asistencias.dto.AsistenciaRequest;
import com.duoc.servicio_asistencias.exception.AsistenciaNotFoundException;
import com.duoc.servicio_asistencias.model.Asistencia;
import com.duoc.servicio_asistencias.repository.AsistenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AsistenciaService {

    @Autowired
    private AsistenciaRepository repository;

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
