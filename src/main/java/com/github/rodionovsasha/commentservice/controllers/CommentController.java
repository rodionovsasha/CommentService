package com.github.rodionovsasha.commentservice.controllers;

import com.github.rodionovsasha.commentservice.entities.Comment;
import com.github.rodionovsasha.commentservice.services.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.github.rodionovsasha.commentservice.Application.API_BASE_URL;

@RestController
@AllArgsConstructor
@RequestMapping(API_BASE_URL + "/comment")
public class CommentController {
    private final CommentService service;

    @GetMapping("/topic/{topicId}")
    public List<Comment> findByTopic(@PathVariable int topicId) {
        return service.findByTopic(topicId);
    }

    @PostMapping("/topic/{topicId}/user/{userId}")
    public ResponseEntity<Comment> add(@Valid @RequestBody Comment comment,
                                       @PathVariable int topicId,
                                       @PathVariable int userId) {
        return new ResponseEntity<>(service.add(comment.getContent(), topicId, userId), HttpStatus.CREATED);
    }

    @PutMapping("/{commentId}/user/{userId}")
    public void update(@Valid @RequestBody Comment comment,
                       @PathVariable int commentId,
                       @PathVariable int userId) {
        service.update(commentId, userId, comment.getContent());
    }
}
