package com.jaime.academix.repository;

import com.jaime.academix.domain.Apunte;
import com.jaime.academix.domain.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ApunteRepository extends JpaRepository<Apunte, Long> {

    Page<Apunte> findByAutorOrderByFechaCreacionDesc(Usuario autor, Pageable pageable);

    Page<Apunte> findByEsPublicoTrueOrderByFechaCreacionDesc(Pageable pageable);

    @Query("SELECT a FROM Apunte a WHERE a.esPublico = true AND " +
            "(LOWER(a.titulo) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
            "LOWER(a.tema.nombre) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
            "LOWER(a.tema.asignatura.nombre) LIKE LOWER(CONCAT('%', :busqueda, '%')))")
    Page<Apunte> buscarApuntesPublicos(@Param("busqueda") String busqueda, Pageable pageable);
}
