package com.jaime.academix.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioResponse {

    private Long id;
    private String nombreUsuario;
    private String correo;
    private String nombreCompleto;
    private String fotoPerfilUrl;
    private String biografia;
    private String carrera;
    private Integer reputacion;
    private Integer nivel;
    private Integer apuntesSubidos;
    private Integer apuntesDescargados;
    private String rol;
    private Set<String> insignias;
    private LocalDateTime fechaCreacion;
}
