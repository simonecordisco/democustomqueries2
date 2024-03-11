package com.customqueries2.democustomqueries2.repository;

import com.customqueries2.democustomqueries2.entities.Flight;
import com.customqueries2.democustomqueries2.entities.FlightStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {
    List<Flight> findByStatus(FlightStatus onTime);

    List<Flight> findByStatusIn(List<FlightStatus> asList);
}