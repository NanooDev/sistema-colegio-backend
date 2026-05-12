package com.duoc.servicio_biblioteca.service;

import com.duoc.servicio_biblioteca.dto.LibroDTO;
import com.duoc.servicio_biblioteca.dto.LibroRequest;
import com.duoc.servicio_biblioteca.exception.LibroNotFoundException;
import com.duoc.servicio_biblioteca.model.Libro;
import com.duoc.servicio_biblioteca.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LibroService {

    @Autowired
    private LibroRepository repository;

    public LibroDTO guardar(LibroRequest request) {
        Libro e = new Libro();
        e.setTitulo(request.getTitulo());
        e.setAutor(request.getAutor());
        e.setEjemplaresDisponibles(request.getEjemplaresDisponibles());
        return convertirADTO(repository.save(e));
    }

    public List<LibroDTO> listar() {
        return repository.findAll().stream().map(this::convertirADTO).collect(Collectors.toList());
    }

    public LibroDTO buscarPorId(Long id) {
        Libro e = repository.findById(id).orElseThrow(() -> new LibroNotFoundException(id));
        return convertirADTO(e);
    }

    public LibroDTO actualizar(Long id, LibroRequest request) {
        Libro ex = repository.findById(id).orElseThrow(() -> new LibroNotFoundException(id));
        ex.setTitulo(request.getTitulo());
        ex.setAutor(request.getAutor());
        ex.setEjemplaresDisponibles(request.getEjemplaresDisponibles());
        return convertirADTO(repository.save(ex));
    }

    public void eliminar(Long id) {
        repository.findById(id).orElseThrow(() -> new LibroNotFoundException(id));
        repository.deleteById(id);
    }

    private LibroDTO convertirADTO(Libro e) {
        LibroDTO dto = new LibroDTO();
        dto.setCategoria("libro");
        dto.setId(e.getId());
        dto.setTitulo(e.getTitulo());
        dto.setAutor(e.getAutor());
        dto.setEjemplaresDisponibles(e.getEjemplaresDisponibles());
        return dto;
    }
}
