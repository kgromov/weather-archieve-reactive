package com.weather_archieve.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class YearsRange {
    private int minYear;
    private int maxYear;
}
