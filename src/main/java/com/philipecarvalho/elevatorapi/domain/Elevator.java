package com.philipecarvalho.elevatorapi.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.philipecarvalho.elevatorapi.exception.ElevatorEmptyException;
import com.philipecarvalho.elevatorapi.exception.ElevatorFullException;
import com.philipecarvalho.elevatorapi.exception.GroundFloorException;
import com.philipecarvalho.elevatorapi.exception.LastFloorException;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "elevators")
public class Elevator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int currentPassengers;
    private int currentFloor;

    @NotNull
    @Min(value = 1, message = "maxCapacity must be greater than zero")
    private Integer maxCapacity;
    @NotNull
    @Min(value = 1, message = "maxFloor must be greater than zero")
    private Integer maxFloor;

    @JsonCreator
    public Elevator(@JsonProperty("maxCapacity") Integer maxCapacity, @JsonProperty("maxFloor") Integer maxFloor) {
        this.currentPassengers = 0;
        this.currentFloor = 0;
        this.maxCapacity = maxCapacity;
        this.maxFloor = maxFloor;
    }

    //JPA requer construtor vazio
    public Elevator() {
    }

    public void addPassenger() {
        if (currentPassengers >= maxCapacity) throw new ElevatorFullException(id);
        currentPassengers ++;
    }

    public void removePassenger() {
        if (currentPassengers <= 0) throw new ElevatorEmptyException(id);
        currentPassengers--;
    }

    public void moveElevatorUp() {
        if (currentFloor >= maxFloor) throw new LastFloorException(id);
        currentFloor++;
    }

    public void moveElevatorDown() {
        if (currentFloor <= 0) throw new GroundFloorException(id);
        currentFloor--;
    }

    public Long getId() {
        return id;
    }

    public int getCurrentPassengers() {
        return currentPassengers;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public Integer getMaxCapacity() {
        return maxCapacity;
    }

    public Integer getMaxFloor() {
        return maxFloor;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
