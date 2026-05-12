package com.duoc.servicio_cursos.repository;

import com.duoc.servicio_cursos.entity.CursoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CursoEntityRepository extends JpaRepository<CursoEntity, Long> {
}
