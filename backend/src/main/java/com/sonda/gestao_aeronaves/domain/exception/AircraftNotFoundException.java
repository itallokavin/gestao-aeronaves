package com.sonda.gestao_aeronaves.domain.exception;

public class AircraftNotFoundException extends RuntimeException {

    public AircraftNotFoundException(Long id) {
        super("Aircraft not found with ID: " + id);
    }

    public AircraftNotFoundException(String message) {
        super(message);
    }
}
