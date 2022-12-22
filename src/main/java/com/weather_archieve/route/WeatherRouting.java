package com.weather_archieve.route;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class WeatherRouting {
    private final WeatherRouteHandler weatherRouteHandler;

    @Bean
    public RouterFunction<ServerResponse> weatherRoutes() {
        return route()
                .GET("/weather/current", accept(APPLICATION_JSON), weatherRouteHandler::getCurrentTemperature)
                .GET("/weather/single/{date}", accept(APPLICATION_JSON), weatherRouteHandler::getTemperatureForDate)
                .build();
    }
}
