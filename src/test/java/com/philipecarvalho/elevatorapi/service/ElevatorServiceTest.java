package com.philipecarvalho.elevatorapi.service;

import com.philipecarvalho.elevatorapi.domain.Elevator;
import com.philipecarvalho.elevatorapi.dto.CreateElevatorRequest;
import com.philipecarvalho.elevatorapi.exception.*;
import com.philipecarvalho.elevatorapi.repository.ElevatorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ElevatorServiceTest {
    @Mock
    private ElevatorRepository repository;
    @InjectMocks
    private ElevatorService service;

    @Test
    void shouldCreateElevatorSuccessfully() {
        CreateElevatorRequest request = new CreateElevatorRequest(2, 2);
        Elevator elevator = new Elevator(2, 2);
        when(repository.save(any(Elevator.class))).thenReturn(elevator);

        Elevator created = service.createUpdate(request);
        assertNotNull(created);
        assertEquals(2, created.getMaxCapacity());
        assertEquals(2, created.getMaxFloor());
        verify(repository).save(any(Elevator.class));
    }

    @Test
    void shouldReturnElevatorBasedOnItId() {
        Long id = 1L;
        Elevator elevator = new Elevator(1, 1);
        elevator.setId(id);
        when(repository.findById(id)).thenReturn(Optional.of(elevator));

        Elevator found = service.findById(id);
        assertNotNull(found);
        assertEquals(id, found.getId());
        verify(repository).findById(id);
    }

    @Test
    void shouldDeleteElevatorWhenIdExists() {
        Long id = 1L;
        Elevator elevator = new Elevator(1, 1);
        elevator.setId(id);
        when(repository.findById(id)).thenReturn(Optional.of(elevator));

        service.deleteById(id);
        verify(repository).deleteById(id);
    }

    @Test
    void shouldNotDeleteElevatorWhenIdDoesNotExist() {
        Long id = 1L;
        when(repository.findById(id)).thenReturn(Optional.empty());
        assertThrows(ElevatorNotFoundException.class, ()->service.deleteById(id));
        verify(repository, never()).deleteById(any());
    }

    @Test
    void shouldThrowExceptionWhenExceedingMaxCapacity() {
        Long id = 1L;
        Elevator elevator = new Elevator(1, 1);
        elevator.setId(id);
        elevator.addPassenger();
        when(repository.findById(id)).thenReturn(Optional.of(elevator));

        assertThrows(ElevatorFullException.class, ()->service.add(id));
        verify(repository, never()).save(any());
    }

    @Test
    void shouldNotRemovePassengerWhenElevatorIsEmpty() {
        Long id = 1L;
        Elevator elevator = new Elevator(1, 1);
        elevator.setId(id);
        when(repository.findById(id)).thenReturn(Optional.of(elevator));

        assertThrows(ElevatorEmptyException.class, ()->service.remove(id));
        verify(repository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenTryingToGoAboveMaxFloor() {
        Long id = 1L;
        Elevator elevator = new Elevator(1, 1);
        elevator.setId(id);
        elevator.moveElevatorUp();
        when(repository.findById(id)).thenReturn(Optional.of(elevator));

        assertThrows(LastFloorException.class, ()->service.moveUp(id));
        verify(repository, never()).save(any());
    }

    @Test
    void shouldNotMoveDownWhenReachTheBottomFloor() {
        Long id = 1L;
        Elevator elevator = new Elevator(1, 1);
        elevator.setId(id);
        when(repository.findById(id)).thenReturn(Optional.of(elevator));

        assertThrows(GroundFloorException.class, ()->service.moveDown(id));
        verify(repository, never()).save(any());
    }
}
