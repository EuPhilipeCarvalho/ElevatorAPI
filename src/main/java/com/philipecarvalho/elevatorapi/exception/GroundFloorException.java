package com.philipecarvalho.elevatorapi.exception;

public class GroundFloorException extends RuntimeException {
    public GroundFloorException(Long id) {
        super("Invalid operation for elevator " + id + ", because it's already at the ground floor");
    }

    public GroundFloorException(String message) {
        super(message);
    }
}
