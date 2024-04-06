package com.github.rodionovsasha.commentservice.controllers;

import com.github.rodionovsasha.commentservice.entities.Topic;
import com.github.rodionovsasha.commentservice.services.TopicService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@AllArgsConstructor
@RequestMapping("/topic")
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

    @ResponseStatus(CREATED)
    @PostMapping("/user/{id}")
    public Topic start(@Valid @RequestBody Topic topic, @PathVariable int id) {
        return service.start(topic.getTitle(), id);
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
