package com.github.rodionovsasha.commentservice.controllers;

import com.github.rodionovsasha.commentservice.entities.Topic;
import com.github.rodionovsasha.commentservice.services.TopicService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.github.rodionovsasha.commentservice.Application.API_BASE_URL;

@RestController
@AllArgsConstructor
@RequestMapping(API_BASE_URL + "/topic")
public class TopicController {
    private final TopicService service;

    @GetMapping("/{id}")
    public Topic getById(@PathVariable long id) {
        return service.getById(id);
    }

    @PostMapping("/user/{id}")
    public ResponseEntity<Topic> start(@Valid @RequestBody Topic topic, @PathVariable long id) {
        return new ResponseEntity<>(service.start(topic.getTitle(), id), HttpStatus.CREATED);
    }
}
