package com.jaime.academix.controller;

import com.jaime.academix.domain.request.ApunteRequest;
import com.jaime.academix.domain.response.ApunteResponse;
import com.jaime.academix.domain.response.MensajeResponse;
import com.jaime.academix.entity.Usuario;
import com.jaime.academix.service.ServicioApunte;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
public class ControladorApunte {

    private final ServicioApunte servicioApunte;

    @PostMapping
    public ResponseEntity<ApunteResponse> crearApunte(
            @Valid @RequestBody ApunteRequest request,
            @AuthenticationPrincipal Usuario autor) {
        return new ResponseEntity<>(servicioApunte.crearApunte(request, autor), HttpStatus.CREATED);
    }

    @GetMapping("/my")
    public ResponseEntity<Page<ApunteResponse>> listarApuntesPropios(
            @AuthenticationPrincipal Usuario autor,
            Pageable pageable) {
        return ResponseEntity.ok(servicioApunte.listarApuntesPropios(autor, pageable));
    }

    @GetMapping("/public")
    public ResponseEntity<Page<ApunteResponse>> listarApuntesPublicos(Pageable pageable) {
        return ResponseEntity.ok(servicioApunte.listarApuntesPublicos(pageable));
    }

    @GetMapping("/public/search")
    public ResponseEntity<Page<ApunteResponse>> buscarApuntesPublicos(
            @RequestParam String q,
            Pageable pageable) {
        return ResponseEntity.ok(servicioApunte.buscarApuntesPublicos(q, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApunteResponse> obtenerApunte(
            @PathVariable Long id,
            @AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(servicioApunte.obtenerApunte(id, usuario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApunteResponse> editarApunte(
            @PathVariable Long id,
            @Valid @RequestBody ApunteRequest request,
            @AuthenticationPrincipal Usuario autor) {
        return ResponseEntity.ok(servicioApunte.editarApunte(id, request, autor));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MensajeResponse> eliminarApunte(
            @PathVariable Long id,
            @AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(servicioApunte.eliminarApunte(id, usuario));
    }

    @PatchMapping("/{id}/publish")
    public ResponseEntity<ApunteResponse> publicarApunte(
            @PathVariable Long id,
            @AuthenticationPrincipal Usuario autor) {
        return ResponseEntity.ok(servicioApunte.publicarApunte(id, autor));
    }

    @PostMapping("/{id}/download")
    public ResponseEntity<ApunteResponse> descargarApunte(
            @PathVariable Long id,
            @AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(servicioApunte.descargarApunte(id, usuario));
    }

    @PostMapping("/{id}/upload-file")
    public ResponseEntity<ApunteResponse> subirArchivo(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile archivo,
            @AuthenticationPrincipal Usuario autor) throws IOException {
        return ResponseEntity.ok(servicioApunte.subirArchivo(id, archivo, autor));
    }
}
