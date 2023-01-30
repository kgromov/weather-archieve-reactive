package com.weather_archieve.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeasonTemperature {
    private Integer year;
    private Season season;
    private Double minTemp;
    private Double maxTemp;
    private Double avgTemp;
}
