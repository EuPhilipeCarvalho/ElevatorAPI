package com.philipecarvalho.elevatorapi.exception;

public class ElevatorEmptyException extends RuntimeException {
    public ElevatorEmptyException(Long id) {
        super("Invalid operation for elevator " + id + ", because there's no one inside");
    }

    public ElevatorEmptyException(String message) {
        super(message);
    }
}
