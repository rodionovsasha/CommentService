package com.github.rodionovsasha.commentservice;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@SpringBootApplication
@Configuration
@EnableSpringDataWebSupport
public class Application {
    public static final String API_BASE_URL = "/v1/api";

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
