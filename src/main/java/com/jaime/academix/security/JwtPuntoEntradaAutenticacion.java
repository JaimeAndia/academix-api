package com.jaime.academix.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jaime.academix.domain.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class JwtPuntoEntradaAutenticacion implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ErrorResponse error = ErrorResponse.builder()
                .estado(401)
                .mensaje("No autorizado: " + authException.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        mapper.writeValue(response.getOutputStream(), error);
    }
}
