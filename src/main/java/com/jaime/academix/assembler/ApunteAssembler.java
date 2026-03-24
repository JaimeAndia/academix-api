package com.jaime.academix.assembler;

import com.jaime.academix.domain.response.ApunteResponse;
import com.jaime.academix.entity.Apunte;
import org.springframework.stereotype.Component;

@Component
public class ApunteAssembler {

    public ApunteResponse aResponse(Apunte apunte) {
        return ApunteResponse.builder()
                .id(apunte.getId())
                .titulo(apunte.getTitulo())
                .descripcion(apunte.getDescripcion())
                .contenido(apunte.getContenido())
                .archivoUrl(apunte.getArchivoUrl())
                .esPublico(apunte.getEsPublico())
                .cantidadDescargas(apunte.getCantidadDescargas())
                .promedioValoracion(apunte.getPromedioValoracion())
                .autorNombreUsuario(apunte.getAutor().getNombreUsuario())
                .autorId(apunte.getAutor().getId())
                .tema(apunte.getTema() != null ? apunte.getTema().getNombre() : null)
                .asignatura(apunte.getTema() != null ? apunte.getTema().getAsignatura().getNombre() : null)
                .carrera(apunte.getTema() != null ? apunte.getTema().getAsignatura().getCarrera().getNombre() : null)
                .fechaCreacion(apunte.getFechaCreacion())
                .fechaActualizacion(apunte.getFechaActualizacion())
                .build();
    }
}
