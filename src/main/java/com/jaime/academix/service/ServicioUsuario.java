package com.jaime.academix.service;

import com.jaime.academix.assembler.UsuarioAssembler;
import com.jaime.academix.domain.request.ActualizarPerfilRequest;
import com.jaime.academix.domain.request.CambiarRolRequest;
import com.jaime.academix.domain.response.MensajeResponse;
import com.jaime.academix.domain.response.UsuarioResponse;
import com.jaime.academix.entity.Carrera;
import com.jaime.academix.entity.Rol;
import com.jaime.academix.entity.Usuario;
import com.jaime.academix.exception.AccesoNoAutorizadoException;
import com.jaime.academix.exception.RecursoNoEncontradoException;
import com.jaime.academix.exception.SolicitudInvalidaException;
import com.jaime.academix.repository.CarreraRepository;
import com.jaime.academix.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ServicioUsuario {

    private final UsuarioRepository usuarioRepository;
    private final CarreraRepository carreraRepository;
    private final UsuarioAssembler usuarioAssembler;

    public UsuarioResponse obtenerPerfilPropio(Usuario usuario) {
        return usuarioAssembler.aResponse(usuario);
    }

    @Transactional
    public UsuarioResponse actualizarPerfil(Usuario usuario, ActualizarPerfilRequest request) {
        if (request.getNombreCompleto() != null) {
            usuario.setNombreCompleto(request.getNombreCompleto());
        }
        if (request.getBiografia() != null) {
            usuario.setBiografia(request.getBiografia());
        }
        if (request.getFotoPerfilUrl() != null) {
            usuario.setFotoPerfilUrl(request.getFotoPerfilUrl());
        }
        if (request.getCarrera() != null) {
            Carrera carrera = carreraRepository.findByNombre(request.getCarrera())
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                            "No se encontró la carrera: " + request.getCarrera()));
            usuario.setCarrera(carrera);
        }

        usuarioRepository.save(usuario);
        return usuarioAssembler.aResponse(usuario);
    }

    public UsuarioResponse obtenerPerfilPublico(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontró el usuario con id: " + id));
        return usuarioAssembler.aResponsePublico(usuario);
    }

    public Page<UsuarioResponse> obtenerRanking(Pageable pageable) {
        return usuarioRepository.findAllByActivoTrueOrderByReputacionDesc(pageable)
                .map(usuarioAssembler::aResponsePublico);
    }

    @Transactional
    public UsuarioResponse cambiarRol(Long id, CambiarRolRequest request) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontró el usuario con id: " + id));

        try {
            Rol nuevoRol = Rol.valueOf(request.getRol().toUpperCase());
            usuario.setRol(nuevoRol);
        } catch (IllegalArgumentException e) {
            throw new SolicitudInvalidaException("Rol no válido: " + request.getRol());
        }

        usuarioRepository.save(usuario);
        return usuarioAssembler.aResponse(usuario);
    }

    @Transactional
    public MensajeResponse desactivarCuenta(Long id, Usuario usuarioActual) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontró el usuario con id: " + id));

        boolean esAdmin = usuarioActual.getRol() == Rol.ADMIN;
        boolean esPropietario = usuarioActual.getId().equals(id);

        if (!esAdmin && !esPropietario) {
            throw new AccesoNoAutorizadoException("No tienes permiso para desactivar esta cuenta");
        }

        usuario.setActivo(false);
        usuarioRepository.save(usuario);
        return new MensajeResponse("Cuenta desactivada correctamente");
    }
}
