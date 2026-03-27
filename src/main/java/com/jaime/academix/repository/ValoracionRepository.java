package com.jaime.academix.repository;

import com.jaime.academix.domain.Apunte;
import com.jaime.academix.domain.Usuario;
import com.jaime.academix.domain.Valoracion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ValoracionRepository extends JpaRepository<Valoracion, Long> {

    Optional<Valoracion> findByUsuarioAndApunte(Usuario usuario, Apunte apunte);

    Boolean existsByUsuarioAndApunte(Usuario usuario, Apunte apunte);

    Page<Valoracion> findByApunteOrderByFechaCreacionDesc(Apunte apunte, Pageable pageable);

    @Query("SELECT AVG(v.puntuacion) FROM Valoracion v WHERE v.apunte = :apunte")
    Double calcularPromedioValoracion(@Param("apunte") Apunte apunte);
}
