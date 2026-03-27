package com.jaime.academix.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApunteRequest {

    @NotBlank(message = "El título es obligatorio")
    private String titulo;

    private String descripcion;

    private String contenido;

    private Long temaId;
}
