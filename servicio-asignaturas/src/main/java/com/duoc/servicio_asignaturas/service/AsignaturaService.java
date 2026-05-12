package com.duoc.servicio_asignaturas.service;

import com.duoc.servicio_asignaturas.dto.AsignaturaDTO;
import com.duoc.servicio_asignaturas.dto.AsignaturaRequest;
import com.duoc.servicio_asignaturas.exception.AsignaturaNotFoundException;
import com.duoc.servicio_asignaturas.model.Asignatura;
import com.duoc.servicio_asignaturas.repository.AsignaturaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AsignaturaService {

    @Autowired
    private AsignaturaRepository repository;

    public AsignaturaDTO guardar(AsignaturaRequest request) {
        Asignatura e = new Asignatura();
        e.setNombre(request.getNombre());
        e.setCodigo(request.getCodigo());
        return convertirADTO(repository.save(e));
    }

    public List<AsignaturaDTO> listar() {
        return repository.findAll().stream().map(this::convertirADTO).collect(Collectors.toList());
    }

    public AsignaturaDTO buscarPorId(Long id) {
        Asignatura e = repository.findById(id).orElseThrow(() -> new AsignaturaNotFoundException(id));
        return convertirADTO(e);
    }

    public AsignaturaDTO actualizar(Long id, AsignaturaRequest request) {
        Asignatura ex = repository.findById(id).orElseThrow(() -> new AsignaturaNotFoundException(id));
        ex.setNombre(request.getNombre());
        ex.setCodigo(request.getCodigo());
        return convertirADTO(repository.save(ex));
    }

    public void eliminar(Long id) {
        repository.findById(id).orElseThrow(() -> new AsignaturaNotFoundException(id));
        repository.deleteById(id);
    }

    private AsignaturaDTO convertirADTO(Asignatura e) {
        AsignaturaDTO dto = new AsignaturaDTO();
        dto.setCategoria("asignatura");
        dto.setId(e.getId());
        dto.setNombre(e.getNombre());
        dto.setCodigo(e.getCodigo());
        return dto;
    }
}
