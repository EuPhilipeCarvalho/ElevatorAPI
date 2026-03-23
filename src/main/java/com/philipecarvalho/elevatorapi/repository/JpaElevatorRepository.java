package com.philipecarvalho.elevatorapi.repository;

import com.philipecarvalho.elevatorapi.domain.Elevator;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

@Profile("prod")
public interface JpaElevatorRepository extends JpaRepository<Elevator, Long>, ElevatorRepository {
}
