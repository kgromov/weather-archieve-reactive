package com.weather_archieve.repository;

import com.weather_archieve.model.DailyTemperature;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

// TODO: add query methods
public interface DailyTemperatureRepository extends ReactiveMongoRepository<DailyTemperature, String> {
    Mono<DailyTemperature> findByDate(LocalDate date);

    Flux<DailyTemperature> findByDateBetween(LocalDate from, LocalDate to);
}
