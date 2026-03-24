package com.jaime.academix.assembler;

import com.jaime.academix.domain.response.UsuarioResponse;
import com.jaime.academix.entity.Insignia;
import com.jaime.academix.entity.Usuario;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UsuarioAssembler {

    public UsuarioResponse aResponse(Usuario usuario) {
        return UsuarioResponse.builder()
                .id(usuario.getId())
                .nombreUsuario(usuario.getNombreUsuario())
                .correo(usuario.getCorreo())
                .nombreCompleto(usuario.getNombreCompleto())
                .fotoPerfilUrl(usuario.getFotoPerfilUrl())
                .biografia(usuario.getBiografia())
                .carrera(usuario.getCarrera() != null ? usuario.getCarrera().getNombre() : null)
                .reputacion(usuario.getReputacion())
                .nivel(usuario.getNivel())
                .apuntesSubidos(usuario.getApuntesSubidos())
                .apuntesDescargados(usuario.getApuntesDescargados())
                .rol(usuario.getRol().name())
                .insignias(usuario.getInsignias().stream()
                        .map(Insignia::getNombre)
                        .collect(Collectors.toSet()))
                .fechaCreacion(usuario.getFechaCreacion())
                .build();
    }

    public UsuarioResponse aResponsePublico(Usuario usuario) {
        return UsuarioResponse.builder()
                .id(usuario.getId())
                .nombreUsuario(usuario.getNombreUsuario())
                .nombreCompleto(usuario.getNombreCompleto())
                .fotoPerfilUrl(usuario.getFotoPerfilUrl())
                .biografia(usuario.getBiografia())
                .carrera(usuario.getCarrera() != null ? usuario.getCarrera().getNombre() : null)
                .reputacion(usuario.getReputacion())
                .nivel(usuario.getNivel())
                .apuntesSubidos(usuario.getApuntesSubidos())
                .rol(usuario.getRol().name())
                .insignias(usuario.getInsignias().stream()
                        .map(Insignia::getNombre)
                        .collect(Collectors.toSet()))
                .fechaCreacion(usuario.getFechaCreacion())
                .build();
    }
}
