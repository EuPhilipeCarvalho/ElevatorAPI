package com.philipecarvalho.elevatorapi.exception;

public class ElevatorFullException extends RuntimeException {
    public ElevatorFullException(Long id) {
        super("Invalid operation for elevator " + id + ", because it's already full");
    }

    public ElevatorFullException(String message) {
        super(message);
    }
}
