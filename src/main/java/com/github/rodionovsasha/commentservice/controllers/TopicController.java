package com.github.rodionovsasha.commentservice.controllers;

import com.github.rodionovsasha.commentservice.entities.Topic;
import com.github.rodionovsasha.commentservice.services.TopicService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.github.rodionovsasha.commentservice.Application.API_BASE_URL;

@RestController
@AllArgsConstructor
@RequestMapping(API_BASE_URL + "/topic")
public class TopicController {
    private final TopicService service;

    @GetMapping("/{id}")
    public Topic getById(@PathVariable int id) {
        return service.getById(id);
    }

    @GetMapping("/active/{id}")
    public Topic getActiveTopic(@PathVariable int id) {
        return service.getActiveTopic(id);
    }

    @PostMapping("/user/{id}")
    public ResponseEntity<Topic> start(@Valid @RequestBody Topic topic, @PathVariable int id) {
        return new ResponseEntity<>(service.start(topic.getTitle(), id), HttpStatus.CREATED);
    }

    @GetMapping("/archive/{topicId}/user/{userId}")
    public void archive(@PathVariable int topicId, @PathVariable int userId) {
        service.archive(topicId, userId);
    }

    @GetMapping("/{id}/check")
    public void checkTopicExists(@PathVariable int id) {
        service.checkTopicExists(id);
    }

    @GetMapping("/user/{userId}")
    public List<Topic> listForUser(@PathVariable int userId,
                                   @SortDefault(sort = "date", direction = Sort.Direction.DESC) Sort sort) {
        return service.listForUser(userId, sort);
    }

    @GetMapping("/search")
    public List<Topic> search(@RequestParam String query, @RequestParam int size) {
        return service.search(query, size);
    }
}
