package com.github.rodionovsasha.commentservice.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.github.rodionovsasha.commentservice.Application.API_BASE_URL;

@RestController
@AllArgsConstructor
@RequestMapping(API_BASE_URL + "/topic")
public class TopicController {
   /* private final TopicService service;

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public Topic getTopic(@PathVariable final long id) throws TopicNotFoundException {
        return service.getTopicById(id);
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Topic> addTopic(@Valid @RequestBody Topic topic) {
        return new ResponseEntity<>(service.addTopic(topic), HttpStatus.CREATED);
    }

    @PutMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity updateTopic(@Valid @RequestBody Topic topic) throws TopicNotFoundException {
        return ResponseEntity.ok(service.updateTopic(topic));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteTopic(@PathVariable final long id) {
        service.deleteTopic(id);
        return ResponseEntity.noContent().build();
    }*/
}
