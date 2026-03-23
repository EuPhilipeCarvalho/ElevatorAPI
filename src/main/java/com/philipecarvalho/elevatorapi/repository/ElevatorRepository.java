package com.philipecarvalho.elevatorapi.repository;

import com.philipecarvalho.elevatorapi.domain.Elevator;

import java.util.List;
import java.util.Optional;

/***
 * <b>Contrato de persistencia. Qualquer implementacao
 * deve seguir estas regras.</b>
 */
public interface ElevatorRepository {
    Elevator save(Elevator elevator);
    Optional<Elevator> findById(Long id);
    List<Elevator> findAll();
    void deleteById(Long id);
}
