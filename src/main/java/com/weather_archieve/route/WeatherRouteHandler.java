package com.weather_archieve.route;

import com.weather_archieve.model.DailyTemperature;
import com.weather_archieve.model.DayTemperature;
import com.weather_archieve.model.YearAverageTemperature;
import com.weather_archieve.repository.DailyTemperatureRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Service
@Slf4j
@RequiredArgsConstructor
public class WeatherRouteHandler {
    private final DailyTemperatureRepository temperatureRepository;

    public Mono<ServerResponse> getCurrentTemperature(ServerRequest request) {
        Mono<DailyTemperature> temperature = temperatureRepository.findByDate(LocalDate.now());
        return ok()
                .contentType(APPLICATION_JSON)
                .body(temperature, DailyTemperature.class);
    }

    public Mono<ServerResponse> getTemperatureForDate(ServerRequest request) {
        LocalDate date = LocalDate.parse(request.pathVariable("date"), DateTimeFormatter.ISO_LOCAL_DATE);
        Mono<DailyTemperature> temperature = temperatureRepository.findByDate(date);
        return ok()
                .contentType(APPLICATION_JSON)
                .body(temperature, DailyTemperature.class);
    }

    public Mono<ServerResponse> getTemperatureForDateInRange(ServerRequest request) {
        LocalDate date = LocalDate.parse(request.pathVariable("date"), DateTimeFormatter.ISO_LOCAL_DATE);
        String monthDay = date.format(DateTimeFormatter.ofPattern("-MM-dd"));
        int years = request.queryParam("years").map(Integer::parseInt).orElse(Integer.MAX_VALUE);
//        Flux<DailyTemperature> temperatureInRange = temperatureRepository.findByDateInRange(monthDay, PageRequest.ofSize(years));
        log.info("date = {}, monthDay = {}, years = {}", date, monthDay, years);
        PageRequest pageable = PageRequest.ofSize(years).withSort(Sort.by("date").ascending());
        Flux<DailyTemperature> temperatureInRange = temperatureRepository.findByDateInRangeAggregation(monthDay, pageable);
        return ok()
                .contentType(APPLICATION_JSON)
                .body(temperatureInRange, DailyTemperature.class);
    }

    public Mono<ServerResponse> getYearAverageTemperature(ServerRequest request) {
        Flux<YearAverageTemperature> temperature = temperatureRepository.getYearAverageTemperature(Sort.by("year").ascending());
        return ok()
                .contentType(APPLICATION_JSON)
                .body(temperature, YearAverageTemperature.class);
    }

    public Mono<ServerResponse> getMinTemperature(ServerRequest request) {
        Mono<DayTemperature> temperature = temperatureRepository.getMinTemperature();
        return ok()
                .contentType(APPLICATION_JSON)
                .body(temperature, DayTemperature.class);
    }

    public Mono<ServerResponse> getMaxTemperature(ServerRequest request) {
        Mono<DayTemperature> temperature = temperatureRepository.getMaxTemperature();
        return ok()
                .contentType(APPLICATION_JSON)
                .body(temperature, DayTemperature.class);
    }

    record Temp(LocalDate date, Double temperature) {
    }
}
