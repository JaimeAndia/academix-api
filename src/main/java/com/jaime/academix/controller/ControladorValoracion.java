package com.jaime.academix.controller;

import com.jaime.academix.domain.request.ValoracionRequest;
import com.jaime.academix.domain.response.MensajeResponse;
import com.jaime.academix.domain.response.ValoracionResponse;
import com.jaime.academix.entity.Usuario;
import com.jaime.academix.service.ServicioValoracion;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notes/{noteId}/ratings")
@RequiredArgsConstructor
public class ControladorValoracion {

    private final ServicioValoracion servicioValoracion;

    @PostMapping
    public ResponseEntity<ValoracionResponse> crearValoracion(
            @PathVariable Long noteId,
            @Valid @RequestBody ValoracionRequest request,
            @AuthenticationPrincipal Usuario usuario) {
        return new ResponseEntity<>(servicioValoracion.crearValoracion(noteId, request, usuario), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<ValoracionResponse> actualizarValoracion(
            @PathVariable Long noteId,
            @Valid @RequestBody ValoracionRequest request,
            @AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(servicioValoracion.actualizarValoracion(noteId, request, usuario));
    }

    @GetMapping
    public ResponseEntity<Page<ValoracionResponse>> listarValoraciones(
            @PathVariable Long noteId,
            Pageable pageable) {
        return ResponseEntity.ok(servicioValoracion.listarValoraciones(noteId, pageable));
    }

    @DeleteMapping
    public ResponseEntity<MensajeResponse> eliminarValoracion(
            @PathVariable Long noteId,
            @AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(servicioValoracion.eliminarValoracion(noteId, usuario));
    }
}
