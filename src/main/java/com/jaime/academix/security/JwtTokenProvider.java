package com.jaime.academix.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secreto;

    @Value("${jwt.access-token-expiration}")
    private long expiracionAccessToken;

    @Value("${jwt.refresh-token-expiration}")
    private long expiracionRefreshToken;

    private SecretKey obtenerClave() {
        byte[] claveBytes = Decoders.BASE64.decode(secreto);
        return Keys.hmacShaKeyFor(claveBytes);
    }

    public String generarAccessToken(Authentication autenticacion) {
        UserDetails usuario = (UserDetails) autenticacion.getPrincipal();
        return generarToken(usuario.getUsername(), expiracionAccessToken);
    }

    public String generarAccessTokenDesdeNombreUsuario(String nombreUsuario) {
        return generarToken(nombreUsuario, expiracionAccessToken);
    }

    public String generarRefreshToken(Authentication autenticacion) {
        UserDetails usuario = (UserDetails) autenticacion.getPrincipal();
        return generarToken(usuario.getUsername(), expiracionRefreshToken);
    }

    public String generarRefreshTokenDesdeNombreUsuario(String nombreUsuario) {
        return generarToken(nombreUsuario, expiracionRefreshToken);
    }

    private String generarToken(String nombreUsuario, long expiracion) {
        Date ahora = new Date();
        Date fechaExpiracion = new Date(ahora.getTime() + expiracion);

        return Jwts.builder()
                .subject(nombreUsuario)
                .issuedAt(ahora)
                .expiration(fechaExpiracion)
                .signWith(obtenerClave())
                .compact();
    }

    public String obtenerNombreUsuario(String token) {
        return Jwts.parser()
                .verifyWith(obtenerClave())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validarToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(obtenerClave())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
