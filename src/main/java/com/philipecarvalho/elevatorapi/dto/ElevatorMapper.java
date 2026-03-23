package com.philipecarvalho.elevatorapi.dto;

import com.philipecarvalho.elevatorapi.domain.Elevator;
import org.springframework.stereotype.Component;

@Component
public class ElevatorMapper {
    public ElevatorResponse toResponse(Elevator elevator) {
        return new ElevatorResponse(
                elevator.getId(),
                elevator.getCurrentPassengers(),
                elevator.getCurrentFloor(),
                elevator.getMaxCapacity(),
                elevator.getMaxFloor()
        );
    }
}
