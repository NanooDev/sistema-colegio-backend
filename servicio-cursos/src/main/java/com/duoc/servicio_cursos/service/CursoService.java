package com.duoc.servicio_cursos.service;

import com.duoc.servicio_cursos.dto.CursoCreateRequest;
import com.duoc.servicio_cursos.model.Curso;

import java.util.List;

public interface CursoService {

    List<Curso> listarCursos();

    Curso crear(CursoCreateRequest request);

    Curso actualizar(Long id, CursoCreateRequest request);

    void eliminar(Long id);

    Curso obtenerCursoConDetalles(Long id);
}
