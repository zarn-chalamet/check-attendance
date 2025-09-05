package com.ai.attendance_check.attendance_service.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    @Qualifier("userServiceWebClient")
    public WebClient userServiceWebClient(WebClient.Builder builder) {
        return builder.baseUrl("http://localhost:8081") // User Service URL
                .build();
    }

    @Bean
    @Qualifier("courseServiceWebClient")
    public WebClient courseServiceWebClient(WebClient.Builder builder) {
        return builder.baseUrl("http://localhost:8082") // Course Service URL
                .build();
    }
}
