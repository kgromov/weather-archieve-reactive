package com.weather_archieve.repository;

import com.weather_archieve.model.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

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
    Mono<YearsRange> getYearsRange();

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

   @Aggregation(pipeline = {
           """
             {
                $project: {
                  _id: null,
                  month:  {
                    $month: "$date",
                  },
                  year: {
                    $year: "$date",
                  },
                  minTemp: {
                    $min: [
                      "$morningTemperature",
                      "$afternoonTemperature",
                      "$nightTemperature",
                    ]
                  },
                  maxTemp: {
                    $max: [
                      "$morningTemperature",
                      "$afternoonTemperature",
                      "$nightTemperature",
                    ]
                  },
                  avgTemp: {
                    $avg: [
                      "$morningTemperature",
                      "$afternoonTemperature",
                      "$nightTemperature",
                    ]
                  }
                }
              }          
           """,
           """
           {
               $project: {
                 year: 1,
                 minTemp: 1,
                 maxTemp: 1,
                 avgTemp: 1,
                 season: {
                   $switch: {
                     branches: [
                       {
                         case: {
                           $or: [
                             {
                               $eq: ["$month", 1],
                             },
                             {
                               $eq: ["$month", 2],
                             },
                             {
                               $eq: ["$month", 12],
                             }
                           ]
                         },
                         then: "WINTER",
                       },
                       {
                         case: {
                           $and: [
                             {
                               $gt: ["$month", 2],
                             },
                             {
                               $lt: ["$month", 6],
                             }
                           ]
                         },
                         then: "SPRING",
                       },
                       {
                         case: {
                           $and: [
                             {
                               $gt: ["$month", 5],
                             },
                             {
                               $lt: ["$month", 9],
                             }
                           ]
                         },
                         then: "SUMMER",
                       },
                       {
                         case: {
                           $and: [
                             {
                               $gt: ["$month", 8],
                             },
                             {
                               $lt: ["$month", 12],
                             }
                           ]
                         },
                         then: "AUTUMN",
                       },
                     ],
                     default: "Not found",
                   },
                 },
               },
             }
           """,
           """
            {
                $group: {
                  _id: {
                    year: "$year",
                    season: "$season",
                  },
                  minTemp: {
                    $min: "$minTemp",
                  },
                  maxTemp: {
                    $max: "$maxTemp",
                  },
                  avgTemp: {
                    $avg: "$avgTemp",
                  }
                }
              }
           """,
           """
            {
               $project: {
                 _id: 0,
                 year: "$_id.year",
                 season: "$_id.season",
                 minTemp: "$minTemp",
                 maxTemp: "$maxTemp",
                 avgTemp: "$avgTemp",
               },
             }
           """
   })
    Flux<SeasonTemperature> getSeasonsTemperature(Pageable pageable);

    default Flux<YearBySeasonTemperature> getYearsBySeasonsTemperature(Pageable pageable) {
        return getSeasonsTemperature(pageable)
                .groupBy(SeasonTemperature::getYear)
                .flatMap(groupByYear -> {
                 /*   Integer year = groupByYear.key();
                    Mono<List<SeasonTemperature>> seasons = groupByYear.collectSortedList(Comparator.comparing(s -> s.getSeason().ordinal()));
                    return Mono.from(seasons).map(s -> new YearBySeasonTemperature(year, s));*/
                    return groupByYear.collectSortedList(Comparator.comparing(s -> s.getSeason().ordinal()))
                            .map(seasons -> new YearBySeasonTemperature(groupByYear.key(), seasons));
                })
                .sort(Comparator.comparing(YearBySeasonTemperature::getYear));
    }


}
