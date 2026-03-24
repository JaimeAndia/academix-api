package com.jaime.academix.domain.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CambiarRolRequest {

    @NotBlank(message = "El rol es obligatorio")
    private String rol;
}
