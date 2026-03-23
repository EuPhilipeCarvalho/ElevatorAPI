package com.philipecarvalho.elevatorapi.repository;

import com.philipecarvalho.elevatorapi.domain.Elevator;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@Profile({"dev", "test"})
public class InMemoryRepository implements ElevatorRepository {
    private final Map<Long, Elevator> map = new HashMap<>();
    private Long nextId = 1L;

    @Override
    public Elevator save(Elevator elevator) {
        if (elevator.getId() == null) elevator.setId(nextId++);
        map.put(elevator.getId(), elevator);
        return elevator;
    }

    @Override
    public Optional<Elevator> findById(Long id) {
        return Optional.ofNullable(map.get(id));
    }

    @Override
    public List<Elevator> findAll() {
        return new ArrayList<>(map.values());
    }

    @Override
    public void deleteById(Long id) {
        map.remove(id);
    }
}
