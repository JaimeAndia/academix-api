package com.jaime.academix.controller;

import com.jaime.academix.domain.request.ActualizarPerfilRequest;
import com.jaime.academix.domain.request.CambiarRolRequest;
import com.jaime.academix.domain.response.MensajeResponse;
import com.jaime.academix.domain.response.UsuarioResponse;
import com.jaime.academix.entity.Usuario;
import com.jaime.academix.service.ServicioUsuario;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class ControladorUsuario {

    private final ServicioUsuario servicioUsuario;

    @GetMapping("/me")
    public ResponseEntity<UsuarioResponse> obtenerPerfilPropio(@AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(servicioUsuario.obtenerPerfilPropio(usuario));
    }

    @PutMapping("/me")
    public ResponseEntity<UsuarioResponse> actualizarPerfil(
            @AuthenticationPrincipal Usuario usuario,
            @RequestBody ActualizarPerfilRequest request) {
        return ResponseEntity.ok(servicioUsuario.actualizarPerfil(usuario, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> obtenerPerfilPublico(@PathVariable Long id) {
        return ResponseEntity.ok(servicioUsuario.obtenerPerfilPublico(id));
    }

    @GetMapping("/ranking")
    public ResponseEntity<Page<UsuarioResponse>> obtenerRanking(Pageable pageable) {
        return ResponseEntity.ok(servicioUsuario.obtenerRanking(pageable));
    }

    @PutMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponse> cambiarRol(
            @PathVariable Long id,
            @Valid @RequestBody CambiarRolRequest request) {
        return ResponseEntity.ok(servicioUsuario.cambiarRol(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MensajeResponse> desactivarCuenta(
            @PathVariable Long id,
            @AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(servicioUsuario.desactivarCuenta(id, usuario));
    }
}
