package com.weather_archieve.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Configuration
@EnableReactiveMongoRepositories
@Slf4j
@RequiredArgsConstructor
public class MongoConfig extends AbstractReactiveMongoConfiguration {
    private final MongoProperties mongoProperties;

    // Recommended way on mongo atlas documentation does not work
//    @Bean
    public MongoClient reactiveMongoClient_() {
        MongoCredential credential = MongoCredential.createCredential(
                mongoProperties.getUsername(),
                mongoProperties.getDatabase(),
                mongoProperties.getPassword()
        );

       MongoClientSettings settings = MongoClientSettings.builder()
                .credential(credential)
                .applyToSslSettings(builder -> builder.enabled(true))
                .applyToClusterSettings(builder ->
                        builder.hosts(List.of(new ServerAddress(mongoProperties.getHost()))))
                .build();
        log.trace("Mongo properties: {}", mongoProperties);
        return MongoClients.create(settings);
    }

    @Bean
    public MongoClient reactiveMongoClient() {
        return MongoClients.create(mongoProperties.getUri());
    }

    @Override
    protected String getDatabaseName() {
        return mongoProperties.getDatabase();
    }

    @Override
    protected Collection<String> getMappingBasePackages() {
        return List.of("com.weather_archieve");
    }
}
