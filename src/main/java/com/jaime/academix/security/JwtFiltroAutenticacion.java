package com.jaime.academix.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFiltroAutenticacion extends OncePerRequestFilter {

    private final JwtProveedorToken jwtProveedorToken;
    private final ServicioDetallesUsuario servicioDetallesUsuario;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = obtenerTokenDeRequest(request);

        if (StringUtils.hasText(token) && jwtProveedorToken.validarToken(token)) {
            String nombreUsuario = jwtProveedorToken.obtenerNombreUsuario(token);
            UserDetails userDetails = servicioDetallesUsuario.loadUserByUsername(nombreUsuario);

            UsernamePasswordAuthenticationToken autenticacion =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            autenticacion.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(autenticacion);
        }

        filterChain.doFilter(request, response);
    }

    private String obtenerTokenDeRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
