package com.weather_archieve.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "weather_archive")
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class DailyTemperature {
    @Id
    private String id;
    private String date;
    private Double morningTemperature;
    private Double afternoonTemperature;
    private Double eveningTemperature;
    private Double nightTemperature;
}
