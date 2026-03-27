package com.jaime.academix.controller;

import com.jaime.academix.domain.request.LoginRequest;
import com.jaime.academix.domain.request.RegistroRequest;
import com.jaime.academix.domain.response.AuthResponse;
import com.jaime.academix.service.AutenticacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AutenticacionController {

    private final AutenticacionService autenticacionService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registrar(@Valid @RequestBody RegistroRequest request) {
        return new ResponseEntity<>(autenticacionService.registrar(request), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(autenticacionService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> renovarToken(@RequestHeader("Refresh-Token") String refreshToken) {
        return ResponseEntity.ok(autenticacionService.renovarToken(refreshToken));
    }
}
