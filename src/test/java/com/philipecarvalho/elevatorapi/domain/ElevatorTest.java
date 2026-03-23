package com.philipecarvalho.elevatorapi.domain;

import com.philipecarvalho.elevatorapi.exception.ElevatorEmptyException;
import com.philipecarvalho.elevatorapi.exception.ElevatorFullException;
import com.philipecarvalho.elevatorapi.exception.GroundFloorException;
import com.philipecarvalho.elevatorapi.exception.LastFloorException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ElevatorTest {
    @Test
    void shouldCreateElevatorSuccessfully() {
        Elevator elevator = new Elevator(1, 1);
        assertEquals(0, elevator.getCurrentPassengers());
        assertEquals(0, elevator.getCurrentFloor());
        assertEquals(1, elevator.getMaxCapacity());
        assertEquals(1, elevator.getMaxFloor());
    }

    @Test
    void shouldAddPassengersSuccessfully() {
        Elevator elevator = new Elevator(1, 1);
        elevator.addPassenger();
        assertEquals(1, elevator.getCurrentPassengers());
    }

    @Test
    void shouldThrowExceptionWhenElevatorIsFull() {
        Elevator elevator = new Elevator(1, 1);
        elevator.addPassenger();
        assertThrows(ElevatorFullException.class, elevator::addPassenger);
    }

    @Test
    void shouldRemovePassengersSuccessfully() {
        Elevator elevator = new Elevator(1, 1);
        elevator.addPassenger();
        elevator.removePassenger();
        assertEquals(0, elevator.getCurrentPassengers());
    }

    @Test
    void shouldThrowExceptionWhenElevatorIsEmpty() {
        Elevator elevator = new Elevator(1, 1);
        assertThrows(ElevatorEmptyException.class, elevator::removePassenger);
    }

    @Test
    void shouldMoveElevatorUpSuccessfully() {
        Elevator elevator = new Elevator(1, 1);
        elevator.moveElevatorUp();
        assertEquals(1, elevator.getCurrentFloor());
    }

    @Test
    void shouldThrowExceptionWhenReachTheTopFloor() {
        Elevator elevator = new Elevator(1, 1);
        elevator.moveElevatorUp();
        assertThrows(LastFloorException.class, elevator::moveElevatorUp);
    }

    @Test
    void shouldMoveElevatorDownSuccessfully() {
        Elevator elevator = new Elevator(1, 1);
        elevator.moveElevatorUp();
        elevator.moveElevatorDown();
        assertEquals(0, elevator.getCurrentFloor());
    }

    @Test
    void shouldThrowExceptionWhenReachTheBottomFloor() {
        Elevator elevator = new Elevator(1, 1);
        assertThrows(GroundFloorException.class, elevator::moveElevatorDown);
    }
}
