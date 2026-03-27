package com.jaime.academix.dto.request;

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
