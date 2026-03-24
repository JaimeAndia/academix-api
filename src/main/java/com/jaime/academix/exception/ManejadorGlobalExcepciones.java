package com.jaime.academix.exception;

import com.jaime.academix.domain.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ManejadorGlobalExcepciones {

    @ExceptionHandler(SolicitudInvalidaException.class)
    public ResponseEntity<ErrorResponse> manejarSolicitudInvalida(SolicitudInvalidaException ex) {
        return construirRespuestaError(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> manejarRecursoNoEncontrado(RecursoNoEncontradoException ex) {
        return construirRespuestaError(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(AccesoNoAutorizadoException.class)
    public ResponseEntity<ErrorResponse> manejarAccesoNoAutorizado(AccesoNoAutorizadoException ex) {
        return construirRespuestaError(HttpStatus.FORBIDDEN, ex.getMessage());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> manejarUsuarioNoEncontrado(UsernameNotFoundException ex) {
        return construirRespuestaError(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> manejarCredencialesIncorrectas(BadCredentialsException ex) {
        return construirRespuestaError(HttpStatus.UNAUTHORIZED, "Credenciales incorrectas");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> manejarValidacion(MethodArgumentNotValidException ex) {
        String errores = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return construirRespuestaError(HttpStatus.BAD_REQUEST, errores);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> manejarExcepcionGeneral(Exception ex) {
        return construirRespuestaError(HttpStatus.INTERNAL_SERVER_ERROR,
                "Error interno del servidor: " + ex.getMessage());
    }

    private ResponseEntity<ErrorResponse> construirRespuestaError(HttpStatus estado, String mensaje) {
        ErrorResponse error = ErrorResponse.builder()
                .estado(estado.value())
                .mensaje(mensaje)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(error, estado);
    }
}
