package com.jaime.academix.config;

import com.jaime.academix.security.JwtFiltroAutenticacion;
import com.jaime.academix.security.JwtPuntoEntradaAutenticacion;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class ConfiguracionSeguridad {

    private final JwtPuntoEntradaAutenticacion puntoEntradaAutenticacion;
    private final JwtFiltroAutenticacion jwtFiltroAutenticacion;

    @Bean
    public PasswordEncoder codificadorContrasena() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager gestorAutenticacion(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain cadenaFiltrosSeguridad(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(puntoEntradaAutenticacion))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/notes/public/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFiltroAutenticacion, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
