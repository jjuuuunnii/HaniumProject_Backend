package com.indi.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.format.DateTimeFormatter;

@Configuration
public class WebConfig {

    @Bean
    public DateTimeFormatter dateTimeFormatter() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
    }
}
