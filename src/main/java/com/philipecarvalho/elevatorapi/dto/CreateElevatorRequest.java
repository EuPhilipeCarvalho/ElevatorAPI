package com.philipecarvalho.elevatorapi.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class CreateElevatorRequest {
    @NotNull
    @Min(value = 1, message = "maxCapacity must be greater than zero")
    private Integer maxCapacity;
    @NotNull
    @Min(value = 1, message = "maxFloor must be greater than zero")
    private Integer maxFloor;

    public CreateElevatorRequest(Integer maxCapacity, Integer maxFloor) {
        this.maxCapacity = maxCapacity;
        this.maxFloor = maxFloor;
    }

    public Integer getMaxCapacity() {
        return maxCapacity;
    }

    public Integer getMaxFloor() {
        return maxFloor;
    }
}
