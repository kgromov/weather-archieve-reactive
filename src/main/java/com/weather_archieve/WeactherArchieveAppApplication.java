package com.weather_archieve;

import com.weather_archieve.config.FrontendClientProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;


@EnableReactiveMongoRepositories
@EnableConfigurationProperties(FrontendClientProperties.class)
@SpringBootApplication
public class WeactherArchieveAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeactherArchieveAppApplication.class, args);
	}

}
