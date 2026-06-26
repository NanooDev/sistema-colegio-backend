package com.duoc.servicio_cursos.service;

import com.duoc.servicio_cursos.client.EstudianteFeign;
import com.duoc.servicio_cursos.client.ProfesorFeign;
import com.duoc.servicio_cursos.dto.CursoCreateRequest;
import com.duoc.servicio_cursos.entity.CursoEntity;
import com.duoc.servicio_cursos.exception.CursoNotFoundException;
import com.duoc.servicio_cursos.model.Curso;
import com.duoc.servicio_cursos.repository.CursoEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CursoService {

    @Autowired
    private CursoEntityRepository cursoEntityRepository;

    @Autowired
    private ProfesorFeign profesorFeign;

    @Autowired
    private EstudianteFeign estudianteFeign;

    public List<Curso> listarCursos() {
        return cursoEntityRepository.findAll().stream()
                .map(this::mapEntityToModel)
                .toList();
    }

    public Curso crear(CursoCreateRequest request) {
        CursoEntity entity = new CursoEntity();
        entity.setNombre(request.getNombre());
        entity.setProfesorJefeId(request.getProfesorJefeId());
        return mapEntityToModel(cursoEntityRepository.save(entity));
    }

    public Curso actualizar(Long id, CursoCreateRequest request) {
        CursoEntity entity = cursoEntityRepository.findById(id)
                .orElseThrow(() -> new CursoNotFoundException(id));
        entity.setNombre(request.getNombre());
        entity.setProfesorJefeId(request.getProfesorJefeId());
        return mapEntityToModel(cursoEntityRepository.save(entity));
    }

    public void eliminar(Long id) {
        CursoEntity entity = cursoEntityRepository.findById(id)
                .orElseThrow(() -> new CursoNotFoundException(id));
        cursoEntityRepository.delete(entity);
    }

    public Curso obtenerCursoConDetalles(Long id) {
        CursoEntity entity = cursoEntityRepository.findById(id)
                .orElseThrow(() -> new CursoNotFoundException(id));
        Curso curso = mapEntityToModel(entity);

        try {
            curso.setProfesorJefe(profesorFeign.obtenerProfesorPorId(curso.getProfesorJefeId()));
        } catch (Exception e) {
            System.err.println("No se pudo obtener el profesor: " + e.getMessage());
        }

        try {
            curso.setEstudiantes(estudianteFeign.obtenerEstudiantesPorCurso(curso.getId()));
        } catch (Exception e) {
            System.err.println("No se pudo obtener los estudiantes: " + e.getMessage());
        }

        return curso;
    }

    private Curso mapEntityToModel(CursoEntity entity) {
        Curso curso = new Curso();
        curso.setId(entity.getId());
        curso.setNombre(entity.getNombre());
        curso.setProfesorJefeId(entity.getProfesorJefeId());
        return curso;
    }
}
