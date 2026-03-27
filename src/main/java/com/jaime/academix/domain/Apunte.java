package com.jaime.academix.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "apuntes")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Apunte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    private String descripcion;

    @Column(columnDefinition = "TEXT")
    private String contenido;

    @Column(name = "archivo_url")
    private String archivoUrl;

    @Builder.Default
    @Column(name = "es_publico", nullable = false)
    private Boolean esPublico = false;

    @Builder.Default
    @Column(name = "cantidad_descargas", nullable = false)
    private Integer cantidadDescargas = 0;

    @Builder.Default
    @Column(name = "promedio_valoracion", nullable = false)
    private Double promedioValoracion = 0.0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id", nullable = false)
    private Usuario autor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tema_id")
    private Tema tema;

    @OneToMany(mappedBy = "apunte", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Valoracion> valoraciones = new ArrayList<>();

    @CreatedDate
    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    @LastModifiedDate
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;
}
