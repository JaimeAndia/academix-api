package com.jaime.academix.controller;

import com.jaime.academix.domain.request.LoginRequest;
import com.jaime.academix.domain.request.RegistroRequest;
import com.jaime.academix.domain.response.RespuestaAuth;
import com.jaime.academix.service.ServicioAutenticacion;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class ControladorAutenticacion {

    private final ServicioAutenticacion servicioAutenticacion;

    @PostMapping("/register")
    public ResponseEntity<RespuestaAuth> registrar(@Valid @RequestBody RegistroRequest request) {
        return new ResponseEntity<>(servicioAutenticacion.registrar(request), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<RespuestaAuth> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(servicioAutenticacion.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<RespuestaAuth> renovarToken(@RequestHeader("Refresh-Token") String refreshToken) {
        return ResponseEntity.ok(servicioAutenticacion.renovarToken(refreshToken));
    }
}
