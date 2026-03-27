package com.jaime.academix.service;

import com.jaime.academix.assembler.ValoracionAssembler;
import com.jaime.academix.dto.request.ValoracionRequest;
import com.jaime.academix.dto.response.MensajeResponse;
import com.jaime.academix.dto.response.ValoracionResponse;
import com.jaime.academix.domain.Apunte;
import com.jaime.academix.domain.Usuario;
import com.jaime.academix.domain.Valoracion;
import com.jaime.academix.exception.ResourceNotFoundException;
import com.jaime.academix.exception.BadRequestException;
import com.jaime.academix.repository.ApunteRepository;
import com.jaime.academix.repository.ValoracionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ValoracionService {

    private final ValoracionRepository valoracionRepository;
    private final ApunteRepository apunteRepository;
    private final ValoracionAssembler valoracionAssembler;
    private final ReputacionService reputacionService;

    @Transactional
    public ValoracionResponse crearValoracion(Long apunteId, ValoracionRequest request, Usuario usuario) {
        Apunte apunte = buscarApuntePorId(apunteId);

        if (apunte.getAutor().getId().equals(usuario.getId())) {
            throw new BadRequestException("No puedes valorar tus propios apuntes");
        }

        if (valoracionRepository.existsByUsuarioAndApunte(usuario, apunte)) {
            throw new BadRequestException("Ya has valorado este apunte. Usa PUT para actualizar tu valoración");
        }

        Valoracion valoracion = Valoracion.builder()
                .puntuacion(request.getPuntuacion())
                .comentario(request.getComentario())
                .usuario(usuario)
                .apunte(apunte)
                .build();

        valoracionRepository.save(valoracion);
        actualizarPromedioApunte(apunte);

        // Reputación al autor del apunte por la valoración recibida
        int puntosAutor = reputacionService.calcularPuntosValoracion(request.getPuntuacion());
        if (puntosAutor > 0) {
            reputacionService.sumarReputacion(apunte.getAutor(), puntosAutor);
        }

        // +1 reputación al usuario que valora (fomenta participación)
        reputacionService.sumarReputacion(usuario, 1);

        return valoracionAssembler.aResponse(valoracion);
    }

    @Transactional
    public ValoracionResponse actualizarValoracion(Long apunteId, ValoracionRequest request, Usuario usuario) {
        Apunte apunte = buscarApuntePorId(apunteId);

        Valoracion valoracion = valoracionRepository.findByUsuarioAndApunte(usuario, apunte)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontró tu valoración para este apunte"));

        int puntuacionAnterior = valoracion.getPuntuacion();
        valoracion.setPuntuacion(request.getPuntuacion());
        valoracion.setComentario(request.getComentario());

        valoracionRepository.save(valoracion);
        actualizarPromedioApunte(apunte);

        // Ajustar reputación del autor: quitar puntos anteriores y sumar nuevos
        int puntosAnteriores = reputacionService.calcularPuntosValoracion(puntuacionAnterior);
        int puntosNuevos = reputacionService.calcularPuntosValoracion(request.getPuntuacion());
        int diferencia = puntosNuevos - puntosAnteriores;
        if (diferencia != 0) {
            reputacionService.sumarReputacion(apunte.getAutor(), diferencia);
        }

        return valoracionAssembler.aResponse(valoracion);
    }

    public Page<ValoracionResponse> listarValoraciones(Long apunteId, Pageable pageable) {
        Apunte apunte = buscarApuntePorId(apunteId);
        return valoracionRepository.findByApunteOrderByFechaCreacionDesc(apunte, pageable)
                .map(valoracionAssembler::aResponse);
    }

    @Transactional
    public MensajeResponse eliminarValoracion(Long apunteId, Usuario usuario) {
        Apunte apunte = buscarApuntePorId(apunteId);

        Valoracion valoracion = valoracionRepository.findByUsuarioAndApunte(usuario, apunte)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontró tu valoración para este apunte"));

        // Quitar reputación del autor que se ganó con esta valoración
        int puntos = reputacionService.calcularPuntosValoracion(valoracion.getPuntuacion());
        if (puntos > 0) {
            reputacionService.sumarReputacion(apunte.getAutor(), -puntos);
        }

        valoracionRepository.delete(valoracion);
        actualizarPromedioApunte(apunte);

        return new MensajeResponse("Valoración eliminada correctamente");
    }

    private void actualizarPromedioApunte(Apunte apunte) {
        Double promedio = valoracionRepository.calcularPromedioValoracion(apunte);
        apunte.setPromedioValoracion(promedio != null ? promedio : 0.0);
        apunteRepository.save(apunte);
    }

    private Apunte buscarApuntePorId(Long id) {
        return apunteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el apunte con id: " + id));
    }
}
