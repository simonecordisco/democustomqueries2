package com.customqueries2.democustomqueries2.controller;

import com.customqueries2.democustomqueries2.entities.Flight;
import com.customqueries2.democustomqueries2.entities.FlightStatus;
import com.customqueries2.democustomqueries2.repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/flights")
public class FlightController {
@Autowired
private FlightRepository flightRepository;

private static final int DEFAULT_NUMBER_OF_FLIGHTS = 100;

@GetMapping("/provision")
public List<Flight> provisionFlights(@RequestParam(required = false, defaultValue = "100") int n) {
        Random random = new Random();
        List<Flight> flights = IntStream.range(0, n)
        .mapToObj(i -> {
        Flight flight = new Flight();
        flight.setDescription(generateRandomString(random, 10));
        flight.setFromAirport(generateRandomString(random, 3));
        flight.setToAirport(generateRandomString(random, 3));
        flight.setStatus(generateRandomStatus(random));
        return flight;
        })
        .collect(Collectors.toList());

        return flightRepository.saveAll(flights);
        }

@GetMapping
public List<Flight> getAllFlightsWithPagination(
@RequestParam(defaultValue = "0") int page,
@RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("fromAirport").ascending());
        Page<Flight> flightPage = flightRepository.findAll(pageable);
        return flightPage.getContent();
        }

@GetMapping("/ontime")
public List<Flight> getOnTimeFlights() {
        return flightRepository.findByStatus(FlightStatus.ON_TIME);
        }

@GetMapping("/custom")
public List<Flight> getCustomFlights(
@RequestParam FlightStatus p1,
@RequestParam FlightStatus p2) {
        return flightRepository.findByStatusIn(Arrays.asList(p1, p2));
        }

private String generateRandomString(Random random, int length) {
        return random.ints(97, 123)
        .limit(length)
        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
        .toString();
        }

private FlightStatus generateRandomStatus(Random random) {
        return FlightStatus.values()[random.nextInt(FlightStatus.values().length)];
        }
        }
