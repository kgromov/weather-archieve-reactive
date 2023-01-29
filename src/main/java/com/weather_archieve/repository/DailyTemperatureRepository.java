package com.weather_archieve.repository;

import com.weather_archieve.model.DailyTemperature;
import com.weather_archieve.model.DayTemperature;
import com.weather_archieve.model.YearAverageTemperature;
import com.weather_archieve.model.YearsRange;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
                				format: "%Y-%m-%d"                				
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

    @Aggregation(pipeline = {
            """
                {
                	$group: {
                		_id: null,
                		min_date: {
                			$min: "$date"              			
                		},
                		max_date: {
                			$max: "$date"               			
                		},
                		
                	},
                	
                }
            """,
            """
                {
                	$project: {
                		minYear: {
                			$year: "$min_date"             			
                		},
                		maxYear: {
                			$year: "$max_date"               			
                		}
                	},
                	
                }
            """
    })
    Flux<YearsRange> getYearsRange();

    @Aggregation(pipeline = {
            """
                        {
                           	$project: {
                           		_id: null,                                 
                           		year: {$year: "$date"},
                           		temp: {
                           			$avg: [
                           				"$morningTemperature",
                           				"$afternoonTemperature",
                           				"$nightTemperature"
                           			]
                           		},
                           		// season: {
                           		//   $switch:
                             //         {
                             //           branches: [
                             //             {
                             //               case: { $or : [ {$eq: [{$month: "$date"}, 1]}, {$eq : [{$month: "$date"}, 2]}, {$eq : ["$month", 12]} ] },
                             //               then: "Winter"
                             //             },
                             //             {
                             //               case: { $and : [ {$gt: [{$month: "$date"}, 2]}, {$lt : [{$month: "$date"}, 6]} ] },
                             //               then: "Spring"
                             //             },
                             //             {
                             //               case: { $and : [ {$gt: [{$month: "$date"}, 5]}, {$lt : [{$month: "$date"}, 9]} ] },
                             //               then: "Summer"
                             //             },
                             //             {
                             //               case: { $and : [ {$gt: [{$month: "$date"}, 8]}, {$lt : [{$month: "$date"}, 12]} ] },
                             //               then: "Autumn"
                             //             },
                             //           ],
                             //           default: "Not found"
                             //         }
                           		// }
                           	}
                           }
            """,
            """
                {
                     $group: {
                       _id: {year: "$year"},
                       temp: {$avg: "$temp"}
                     }
                }
            """,
            """
               {
                    $project: {
                      _id: 0,
                      year: "$_id.year",
                      temperature: "$temp"
                    }
               }
           """
    })
    Flux<YearAverageTemperature> getYearAverageTemperature(Sort sort);

    @Aggregation(pipeline = {
            """
                {
                   	$project: {
                   		_id: null,
                   		date: {
                   			$dateToString: {
                   				date: "$date",
                   				format: "%Y-%m-%d"
                   			}
                   		},
                   		temperature: {
                   			$min: [
                   				"$morningTemperature",
                   				"$afternoonTemperature",
                   				"$nightTemperature"
                   			]
                   		},
                   	}
                   }
            """,
            """
                {
                	$sort: {
                		temperature: 1
                	}       	
                }
            """,
            """
                {
                	$limit: 1   	
                }
            """
    })
    Mono<DayTemperature> getMinTemperature();

    @Aggregation(pipeline = {
            """
                {
                   	$project: {
                   		_id: null,
                   		date: {
                   			$dateToString: {
                   				date: "$date",
                   				format: "%Y-%m-%d"
                   			}
                   		},
                   		temperature: {
                   			$max: [
                   				"$morningTemperature",
                   				"$afternoonTemperature",
                   				"$nightTemperature"
                   			]
                   		},
                   	}
                   }
            """,
            """
                {
                	$sort: {
                		temperature: -1
                	}	
                }
            """,
            """
                {
                	$limit: 1
                }
            """
    })
    Mono<DayTemperature> getMaxTemperature();

    // TODO: avf season per year
}
