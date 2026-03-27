package com.jaime.academix.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValoracionResponse {

    private Long id;
    private Integer puntuacion;
    private String comentario;
    private String nombreUsuario;
    private Long usuarioId;
    private LocalDateTime fechaCreacion;
}
