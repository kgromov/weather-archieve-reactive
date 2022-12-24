package com.weather_archieve.repository;

import com.weather_archieve.model.DailyTemperature;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.YearMonth;

// TODO: add query methods
public interface DailyTemperatureRepository extends ReactiveMongoRepository<DailyTemperature, String> {
    Mono<DailyTemperature> findByDate(LocalDate date);

    @Query("{'date': {$regex: ?0}}")
    Flux<DailyTemperature> findByDateInRange(String monthDay, Pageable pageable);
}
