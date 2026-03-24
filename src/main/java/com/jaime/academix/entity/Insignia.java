package com.jaime.academix.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "insignias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Insignia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    private String descripcion;

    @Column(name = "icono_url")
    private String iconoUrl;

    @Column(name = "reputacion_requerida", nullable = false)
    private Integer reputacionRequerida;
}
