package com.weather_archieve.route;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.queryParam;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class WeatherRouting {
    private final WeatherRouteHandler weatherRouteHandler;

    @Bean
    public RouterFunction<ServerResponse> weatherRoutes() {
        return route()
                .GET("/weather/range", accept(APPLICATION_JSON), weatherRouteHandler::getYearsRange)
                .GET("/weather/average", accept(APPLICATION_JSON), weatherRouteHandler::getYearAverageTemperature)
                .GET("/weather/min", accept(APPLICATION_JSON), weatherRouteHandler::getMinTemperature)
                .GET("/weather/max", accept(APPLICATION_JSON), weatherRouteHandler::getMaxTemperature)
                .GET("/weather/seasonsInYear", accept(APPLICATION_JSON), weatherRouteHandler::getYearsBySeasonsTemperature)
                .GET("/weather/seasons", accept(APPLICATION_JSON), weatherRouteHandler::getSeasonsTemperature)

                .GET("/weather/current", accept(APPLICATION_JSON), weatherRouteHandler::getCurrentTemperature)
                .GET("/weather/single/{date}", accept(APPLICATION_JSON), weatherRouteHandler::getTemperatureForDate)
                .GET("/weather/{date}", accept(APPLICATION_JSON), weatherRouteHandler::getTemperatureForDateInRange)
                .GET("/weather/{date}", RequestPredicates.all()
                                .and(queryParam("years", v -> true))
                                .and(accept(APPLICATION_JSON)),
                        weatherRouteHandler::getTemperatureForDateInRange
                )
                .build();
    }
}
