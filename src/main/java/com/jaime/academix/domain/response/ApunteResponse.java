package com.jaime.academix.domain.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApunteResponse {

    private Long id;
    private String titulo;
    private String descripcion;
    private String contenido;
    private String archivoUrl;
    private Boolean esPublico;
    private Integer cantidadDescargas;
    private Double promedioValoracion;
    private String autorNombreUsuario;
    private Long autorId;
    private String tema;
    private String asignatura;
    private String carrera;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
}
