package com.github.rodionovsasha.commentservice.config;

import com.github.vkorobkov.jfixtures.JFixtures;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan("com.github.rodionovsasha.commentservice")
public class TestConfig {
    @Bean
    CommandLineRunner runner() {
        return args -> JFixtures.sql99("src/test/resources/yaml/user").toFile("src/test/resources/data.sql");
    }
}
