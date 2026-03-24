package com.jaime.academix.domain.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {

    private int estado;
    private String mensaje;
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
}
