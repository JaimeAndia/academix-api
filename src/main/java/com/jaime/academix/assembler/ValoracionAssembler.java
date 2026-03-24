package com.jaime.academix.assembler;

import com.jaime.academix.domain.response.ValoracionResponse;
import com.jaime.academix.entity.Valoracion;
import org.springframework.stereotype.Component;

@Component
public class ValoracionAssembler {

    public ValoracionResponse aResponse(Valoracion valoracion) {
        return ValoracionResponse.builder()
                .id(valoracion.getId())
                .puntuacion(valoracion.getPuntuacion())
                .comentario(valoracion.getComentario())
                .nombreUsuario(valoracion.getUsuario().getNombreUsuario())
                .usuarioId(valoracion.getUsuario().getId())
                .fechaCreacion(valoracion.getFechaCreacion())
                .build();
    }
}
