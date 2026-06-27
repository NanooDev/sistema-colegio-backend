package com.duoc.servicio_finanzas.service;

import com.duoc.servicio_finanzas.client.EstudianteFeign;
import com.duoc.servicio_finanzas.dto.CargoDTO;
import com.duoc.servicio_finanzas.dto.CargoRequest;
import com.duoc.servicio_finanzas.dto.EstudianteDTO;
import com.duoc.servicio_finanzas.exception.CargoNotFoundException;
import com.duoc.servicio_finanzas.model.Cargo;
import com.duoc.servicio_finanzas.repository.CargoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CargoService {

    private static final Logger log = LoggerFactory.getLogger(CargoService.class);

    @Autowired
    private CargoRepository repository;

    @Autowired
    private EstudianteFeign estudianteFeign;

    public CargoDTO guardar(CargoRequest request) {
        Cargo e = new Cargo();
        e.setEstudianteId(request.getEstudianteId());
        e.setConcepto(request.getConcepto());
        e.setMonto(request.getMonto());
        e.setFechaPago(request.getFechaPago());
        return convertirADTO(repository.save(e));
    }

    public List<CargoDTO> listar() {
        return repository.findAll().stream().map(this::convertirADTO).collect(Collectors.toList());
    }

    public CargoDTO buscarPorId(Long id) {
        Cargo e = repository.findById(id).orElseThrow(() -> new CargoNotFoundException(id));
        return convertirADTO(e);
    }

    public CargoDTO actualizar(Long id, CargoRequest request) {
        Cargo ex = repository.findById(id).orElseThrow(() -> new CargoNotFoundException(id));
        ex.setEstudianteId(request.getEstudianteId());
        ex.setConcepto(request.getConcepto());
        ex.setMonto(request.getMonto());
        ex.setFechaPago(request.getFechaPago());
        return convertirADTO(repository.save(ex));
    }

    public void eliminar(Long id) {
        repository.findById(id).orElseThrow(() -> new CargoNotFoundException(id));
        repository.deleteById(id);
    }

    public CargoDTO obtenerCargoConEstudiante(Long id) {
        Cargo e = repository.findById(id).orElseThrow(() -> new CargoNotFoundException(id));
        CargoDTO dto = convertirADTO(e);

        try {
            EstudianteDTO estudiante = estudianteFeign.obtenerEstudiantePorId(e.getEstudianteId());
            dto.setNombreEstudiante(estudiante.getNombre() + " " + estudiante.getApellido());
        } catch (Exception ex) {
            log.error("No se pudo obtener el estudiante con ID {}: {}", e.getEstudianteId(), ex.getMessage());
        }

        return dto;
    }

    private CargoDTO convertirADTO(Cargo e) {
        CargoDTO dto = new CargoDTO();
        dto.setCategoria("cargo");
        dto.setId(e.getId());
        dto.setEstudianteId(e.getEstudianteId());
        dto.setConcepto(e.getConcepto());
        dto.setMonto(e.getMonto());
        dto.setFechaPago(e.getFechaPago());
        return dto;
    }
}
