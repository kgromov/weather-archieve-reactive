package com.weather_archieve.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class YearBySeasonTemperature {
    private Integer year;
    private List<SeasonTemperature> seasons;
}
