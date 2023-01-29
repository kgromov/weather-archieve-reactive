package com.weather_archieve.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonProperty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class YearAverageTemperature {
    @BsonProperty("year")
    private Integer year;
    private Double temperature;
}
