package com.philipecarvalho.elevatorapi.controller;

import com.philipecarvalho.elevatorapi.domain.Elevator;
import com.philipecarvalho.elevatorapi.dto.CreateElevatorRequest;
import com.philipecarvalho.elevatorapi.dto.ElevatorMapper;
import com.philipecarvalho.elevatorapi.dto.ElevatorResponse;
import com.philipecarvalho.elevatorapi.service.ElevatorService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/elevators")
public class ElevatorController {
    private final ElevatorService service;
    private final ElevatorMapper mapper;
    public ElevatorController(ElevatorService service, ElevatorMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Operation(summary = "Create Elevator",
            description = """
                    Create an elevator based on validation of the fields maxCapacity
                    and maxFloor.""")
    @PostMapping
    public ResponseEntity<ElevatorResponse> create(@Valid @RequestBody CreateElevatorRequest request) {
        Elevator elevator = service.createUpdate(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(elevator));
    }

    @Operation(summary = "Find Elevator",
            description = "Find an elevator based on it's id.")
    @GetMapping("/{id}")
    public ResponseEntity<ElevatorResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toResponse(service.findById(id)));
    }

    @Operation(summary = "List Elevators",
            description = "List all elevators saved on repository.")
    @GetMapping
    public ResponseEntity<List<ElevatorResponse>> findAll() {
        return ResponseEntity.ok(
                service.findAll().stream().map(mapper::toResponse).toList());
    }

    @Operation(summary = "Delete Elevators",
            description = "Delete an elevator based on it's id.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Add Passengers",
            description = "Add a passenger to the elevator based on it's id.")
    @PatchMapping("/{id}/add")
    public ResponseEntity<ElevatorResponse> add(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toResponse(service.add(id)));
    }

    @Operation(summary = "Remove Passengers",
            description = "Remove a passenger from the elevator based on it's id.")
    @PatchMapping("/{id}/remove")
    public ResponseEntity<ElevatorResponse> remove(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toResponse(service.remove(id)));
    }

    @Operation(summary = "Move Up",
            description = "Move elevator up based on it's id.")
    @PatchMapping("/{id}/up")
    public ResponseEntity<ElevatorResponse> moveUp(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toResponse(service.moveUp(id)));
    }

    @Operation(summary = "Move Down",
            description = "Move elevator down based on it's id.")
    @PatchMapping("/{id}/down")
    public ResponseEntity<ElevatorResponse> moveDown(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toResponse(service.moveDown(id)));
    }
}
