package com.github.rodionovsasha.commentservice.config;

import com.github.vkorobkov.jfixtures.JFixtures;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@EnableAutoConfiguration
@ComponentScan("com.github.rodionovsasha.commentservice")
public class TestConfig {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Bean
    CommandLineRunner runner() {
        return args -> {
            String sql = JFixtures.sql99("src/test/resources/yaml/user").asString();
            jdbcTemplate.execute(sql);
        };
    }
}
