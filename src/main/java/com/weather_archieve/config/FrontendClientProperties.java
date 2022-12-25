package com.weather_archieve.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "frontend.client")
@Getter
@Setter
public class FrontendClientProperties {
    private String host;
    private int port;
    private String uri;
    private String[] allowedMethods;
}
