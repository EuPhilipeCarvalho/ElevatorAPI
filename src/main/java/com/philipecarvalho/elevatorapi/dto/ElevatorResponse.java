package com.philipecarvalho.elevatorapi.dto;

public class ElevatorResponse {
    private Long id;
    private int currentPassengers;
    private int currentFloor;
    private int maxCapacity;
    private int maxFloor;

    public ElevatorResponse(Long id, int currentPassengers, int currentFloor, int maxCapacity, int maxFloor) {
        this.id = id;
        this.currentPassengers = currentPassengers;
        this.currentFloor = currentFloor;
        this.maxCapacity = maxCapacity;
        this.maxFloor = maxFloor;
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

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public int getMaxFloor() {
        return maxFloor;
    }
}
