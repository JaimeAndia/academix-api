package com.jaime.academix.controller;

import com.jaime.academix.dto.request.ActualizarPerfilRequest;
import com.jaime.academix.dto.request.CambiarRolRequest;
import com.jaime.academix.dto.response.MensajeResponse;
import com.jaime.academix.dto.response.UsuarioResponse;
import com.jaime.academix.domain.Usuario;
import com.jaime.academix.service.UsuarioService;
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
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping("/me")
    public ResponseEntity<UsuarioResponse> obtenerPerfilPropio(@AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(usuarioService.obtenerPerfilPropio(usuario));
    }

    @PutMapping("/me")
    public ResponseEntity<UsuarioResponse> actualizarPerfil(
            @AuthenticationPrincipal Usuario usuario,
            @RequestBody ActualizarPerfilRequest request) {
        return ResponseEntity.ok(usuarioService.actualizarPerfil(usuario, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> obtenerPerfilPublico(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.obtenerPerfilPublico(id));
    }

    @GetMapping("/ranking")
    public ResponseEntity<Page<UsuarioResponse>> obtenerRanking(Pageable pageable) {
        return ResponseEntity.ok(usuarioService.obtenerRanking(pageable));
    }

    @PutMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponse> cambiarRol(
            @PathVariable Long id,
            @Valid @RequestBody CambiarRolRequest request) {
        return ResponseEntity.ok(usuarioService.cambiarRol(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MensajeResponse> desactivarCuenta(
            @PathVariable Long id,
            @AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(usuarioService.desactivarCuenta(id, usuario));
    }
}
