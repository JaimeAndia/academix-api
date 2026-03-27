package com.jaime.academix.service;

import com.jaime.academix.entity.Insignia;
import com.jaime.academix.entity.Usuario;
import com.jaime.academix.repository.InsigniaRepository;
import com.jaime.academix.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReputacionService {

    private final UsuarioRepository usuarioRepository;
    private final InsigniaRepository insigniaRepository;

    public int calcularNivel(int reputacion) {
        if (reputacion >= 1000) return 5;  // Maestro
        if (reputacion >= 400) return 4;   // Experto
        if (reputacion >= 150) return 3;   // Contribuidor
        if (reputacion >= 50) return 2;    // Estudiante
        return 1;                           // Novato
    }

    @Transactional
    public void sumarReputacion(Usuario usuario, int puntos) {
        usuario.setReputacion(usuario.getReputacion() + puntos);
        usuario.setNivel(calcularNivel(usuario.getReputacion()));
        actualizarInsignias(usuario);
        usuarioRepository.save(usuario);
    }

    @Transactional
    public void actualizarInsignias(Usuario usuario) {
        List<Insignia> insigniasDisponibles =
                insigniaRepository.findByReputacionRequeridaLessThanEqual(usuario.getReputacion());
        usuario.setInsignias(new HashSet<>(insigniasDisponibles));
    }

    public int calcularPuntosValoracion(int puntuacion) {
        return switch (puntuacion) {
            case 5 -> 5;
            case 4 -> 3;
            case 3 -> 1;
            default -> 0;
        };
    }
}
