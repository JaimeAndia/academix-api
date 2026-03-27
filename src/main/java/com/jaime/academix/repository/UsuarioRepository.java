package com.jaime.academix.repository;

import com.jaime.academix.domain.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByNombreUsuario(String nombreUsuario);

    Optional<Usuario> findByCorreo(String correo);

    Boolean existsByNombreUsuario(String nombreUsuario);

    Boolean existsByCorreo(String correo);

    Page<Usuario> findAllByActivoTrueOrderByReputacionDesc(Pageable pageable);
}
