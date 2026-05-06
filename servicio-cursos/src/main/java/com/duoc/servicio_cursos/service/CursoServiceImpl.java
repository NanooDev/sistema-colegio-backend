package com.duoc.servicio_cursos.service;

import com.duoc.servicio_cursos.client.EstudianteFeign;
import com.duoc.servicio_cursos.client.ProfesorFeign;
import com.duoc.servicio_cursos.model.Curso;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CursoServiceImpl implements CursoService {

    @Autowired
    private ProfesorFeign profesorFeign;

    @Autowired
    private EstudianteFeign estudianteFeign;

    @Override
    public Curso obtenerCursoConDetalles(Long id) {
        // En un caso real buscarías el curso en la BD del servicio de cursos.
        // Aquí hacemos una simulación para tu profesor:
        Curso curso = new Curso();
        curso.setId(id);
        curso.setNombre("Matemáticas Avanzadas");
        curso.setProfesorJefeId(1L);

        // Consume el servicio de Profesores usando Feign
        try {
            var profesor = profesorFeign.obtenerProfesorPorId(curso.getProfesorJefeId());
            curso.setProfesorJefe(profesor);
        } catch (Exception e) {
            System.err.println("No se pudo obtener el profesor");
        }

        // Consume el servicio de Estudiantes usando Feign
        try {
            var estudiantes = estudianteFeign.obtenerEstudiantesPorCurso(curso.getId());
            curso.setEstudiantes(estudiantes);
        } catch (Exception e) {
            System.err.println("No se pudo obtener los estudiantes");
        }

        return curso;
    }
}