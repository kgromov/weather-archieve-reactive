package com.weather_archieve.repository;

import com.weather_archieve.model.DailyTemperature;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

public interface DailyTemperatureRepository extends ReactiveMongoRepository<DailyTemperature, String> {
    Mono<DailyTemperature> findByDate(LocalDate date);

    @Query("{'date': {$regex: ?0}}")
    Flux<DailyTemperature> findByDateInRange(String monthDay, Pageable pageable);

    @Aggregation(pipeline = {
            """
                {
                	$project: {
                		_id: 1,
                		morningTemperature: 1,
                		afternoonTemperature: 1,
                		eveningTemperature: 1,
                		nightTemperature: 1,
                		date: {
                			$dateToString: {
                				date: "$date",
                				format: "%Y-%m-%d",
                				
                			}
                		}
                	}
                }
            """,
            """
                {
                	$match: {
                		date: {
                			$regex: ?0
                		}
                	}
                }/*,
                {
                	$sort: {
                		date: 1
                	}
                },
                {
                	$limit: ?1
                }*/
            """
    })
    Flux<DailyTemperature> findByDateInRangeAggregation(String monthDay, Pageable pageable);
}
