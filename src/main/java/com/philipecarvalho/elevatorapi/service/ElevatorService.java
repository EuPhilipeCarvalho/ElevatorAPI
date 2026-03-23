package com.philipecarvalho.elevatorapi.service;

import com.philipecarvalho.elevatorapi.domain.Elevator;
import com.philipecarvalho.elevatorapi.dto.CreateElevatorRequest;
import com.philipecarvalho.elevatorapi.exception.ElevatorNotFoundException;
import com.philipecarvalho.elevatorapi.repository.ElevatorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ElevatorService {
    private final ElevatorRepository repository;
    public ElevatorService(ElevatorRepository repository) {this.repository = repository;}

    public Elevator createUpdate(CreateElevatorRequest request) {
        Elevator elevator = new Elevator(
                request.getMaxCapacity(),
                request.getMaxFloor()
        );
        return repository.save(elevator);
    }

    public Elevator findById(Long id) {
        return repository.findById(id).orElseThrow(()-> new ElevatorNotFoundException(id));
    }

    public List<Elevator> findAll() {
        return repository.findAll();
    }

    public void deleteById(Long id) {
        findById(id);
        repository.deleteById(id);
    }

    public Elevator add(Long id) {
        Elevator elevator = findById(id);
        elevator.addPassenger();
        return repository.save(elevator);
    }

    public Elevator remove(Long id) {
        Elevator elevator = findById(id);
        elevator.removePassenger();
        return repository.save(elevator);
    }

    public Elevator moveUp(Long id) {
        Elevator elevator = findById(id);
        elevator.moveElevatorUp();
        return repository.save(elevator);
    }

    public Elevator moveDown(Long id) {
        Elevator elevator = findById(id);
        elevator.moveElevatorDown();
        return repository.save(elevator);
    }
}
