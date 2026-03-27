package com.jaime.academix.repository;

import com.jaime.academix.domain.Asignatura;
import com.jaime.academix.domain.Carrera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AsignaturaRepository extends JpaRepository<Asignatura, Long> {

    List<Asignatura> findByCarrera(Carrera carrera);
}
