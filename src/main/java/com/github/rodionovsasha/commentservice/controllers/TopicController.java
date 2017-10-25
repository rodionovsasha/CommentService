package com.github.rodionovsasha.commentservice.controllers;

import com.github.rodionovsasha.commentservice.entities.Topic;
import com.github.rodionovsasha.commentservice.services.TopicService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
