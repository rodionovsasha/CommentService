package com.github.rodionovsasha.commentservice.controllers;

import com.github.rodionovsasha.commentservice.entities.Topic;
import com.github.rodionovsasha.commentservice.exceptions.TopicNotFoundException;
import com.github.rodionovsasha.commentservice.services.TopicService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.github.rodionovsasha.commentservice.AppConfig.API_BASE_URL;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@AllArgsConstructor
@RequestMapping(API_BASE_URL)
public class TopicController {
    private static final String TOPIC_BASE_PATH = "/topic";
    private final TopicService service;

    @ApiOperation("Get topic by ID")
    @GetMapping(value = TOPIC_BASE_PATH + "/{id}", produces = APPLICATION_JSON_VALUE)
    public Topic getTopic(@PathVariable final long id) throws TopicNotFoundException {
        return service.getTopicById(id);
    }

    @ApiOperation("Add topic")
    @PostMapping(value = TOPIC_BASE_PATH, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Topic> addTopic(@Valid @RequestBody Topic topic) {
        return new ResponseEntity<>(service.addTopic(topic), HttpStatus.CREATED);
    }

    @ApiOperation("Update topic")
    @PutMapping(value = TOPIC_BASE_PATH, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity updateTopic(@Valid @RequestBody Topic topic) throws TopicNotFoundException {
        return ResponseEntity.ok(service.updateTopic(topic));
    }

    @ApiOperation("Delete topic")
    @DeleteMapping(TOPIC_BASE_PATH + "/{id}")
    public ResponseEntity deleteTopic(@PathVariable final long id) {
        service.deleteTopic(id);
        return ResponseEntity.noContent().build();
    }
}
