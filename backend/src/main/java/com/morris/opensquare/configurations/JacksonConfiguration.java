package com.morris.opensquare.configurations;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Configuration to create a {@link Bean} for Hibernate Module for Jackson. SpringBoot's
 * {@link org.springframework.http.converter.json.Jackson2ObjectMapperBuilder} will use
 * this via autoconfiguration, and all ObjectMapper instance will use the Spring Boot
 * defaults plus customizations.
 */
@Configuration
public class JacksonConfiguration {

    @Bean
    public Module hibernateModule() {
        return new Hibernate6Module();
    }

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        return JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build();
    }
}
