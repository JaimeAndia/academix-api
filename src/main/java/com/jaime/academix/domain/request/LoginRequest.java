package com.jaime.academix.domain.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequest {

    @NotBlank(message = "El usuario o correo es obligatorio")
    private String usuarioOCorreo;

    @NotBlank(message = "La contraseña es obligatoria")
    private String contrasena;
}
