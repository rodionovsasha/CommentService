package com.github.rodionovsasha.commentservice;

import com.github.rodionovsasha.commentservice.entities.Comment;
import com.github.rodionovsasha.commentservice.entities.Topic;
import com.github.rodionovsasha.commentservice.entities.User;
import com.github.rodionovsasha.commentservice.repositories.CommentRepository;
import com.github.rodionovsasha.commentservice.repositories.TopicRepository;
import com.github.rodionovsasha.commentservice.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static java.util.Arrays.asList;

@Configuration
@EnableSwagger2
public class AppConfig {
    public static final String API_BASE_URL = "/v1/api";

    @Bean
    CommandLineRunner runner(UserRepository userRepository, TopicRepository topicRepository, CommentRepository commentRepository) {
        User user = userRepository.save(new User("Homer", 39));
        return args -> asList("Topic 1", "Topic 2").forEach(topicName -> {
                    Topic topic = topicRepository.save(new Topic(topicName, user));
                    commentRepository.save(new Comment("Comment 1", user, topic));
                    commentRepository.save(new Comment("Comment 2", user, topic));
                }
        );
    }

    @Bean
    protected Docket swagger() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.github.rodionovsasha.commentservice.controllers"))
                .paths(PathSelectors.ant(API_BASE_URL + "/**"))
                .build();
    }
}
