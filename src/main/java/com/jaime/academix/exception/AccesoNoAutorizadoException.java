package com.jaime.academix.exception;

public class AccesoNoAutorizadoException extends RuntimeException {

    public AccesoNoAutorizadoException(String mensaje) {
        super(mensaje);
    }
}
