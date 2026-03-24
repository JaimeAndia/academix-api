package com.jaime.academix.domain.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActualizarPerfilRequest {

    private String nombreCompleto;
    private String biografia;
    private String carrera;
    private String fotoPerfilUrl;
}
