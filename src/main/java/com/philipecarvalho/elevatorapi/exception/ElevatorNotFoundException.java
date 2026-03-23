package com.philipecarvalho.elevatorapi.exception;

public class ElevatorNotFoundException extends RuntimeException {
    public ElevatorNotFoundException(Long id) {
        super("Elevator with id " + id + " not found");
    }

    public ElevatorNotFoundException(String message) {
        super(message);
    }
}
