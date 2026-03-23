package com.philipecarvalho.elevatorapi.exception;

public class LastFloorException extends RuntimeException {
    public LastFloorException(Long id) {
        super("Invalid operation for elevator " + id + ", because it's already at the top floor");
    }

    public LastFloorException(String message) {
        super(message);
    }
}
