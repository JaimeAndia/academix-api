package com.jaime.academix.repository;

import com.jaime.academix.domain.Asignatura;
import com.jaime.academix.domain.Tema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TemaRepository extends JpaRepository<Tema, Long> {

    List<Tema> findByAsignatura(Asignatura asignatura);
}
