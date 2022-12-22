package com.weather_archieve;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@EnableReactiveMongoRepositories
@SpringBootApplication
public class WeactherArchieveAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeactherArchieveAppApplication.class, args);
	}

}
