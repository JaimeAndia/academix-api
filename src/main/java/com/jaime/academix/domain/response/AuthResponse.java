package com.jaime.academix.domain.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {

    private String accessToken;
    private String refreshToken;
    @Builder.Default
    private String tipoToken = "Bearer";
    private UsuarioResponse usuario;
}
