package com.github.rodionovsasha.commentservice;

import com.github.rodionovsasha.commentservice.repositories.CommentRepository;
import com.github.rodionovsasha.commentservice.repositories.TopicRepository;
import com.github.rodionovsasha.commentservice.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    public static final String API_BASE_URL = "/v1/api";

    @Bean
    CommandLineRunner runner(UserRepository userRepository, TopicRepository topicRepository, CommentRepository commentRepository) {
        //here load demo data before running the application
        return null;
    }
}
