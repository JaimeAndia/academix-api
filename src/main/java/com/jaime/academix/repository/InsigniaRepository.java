package com.jaime.academix.repository;

import com.jaime.academix.domain.Insignia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InsigniaRepository extends JpaRepository<Insignia, Long> {

    List<Insignia> findByReputacionRequeridaLessThanEqual(Integer reputacion);
}
