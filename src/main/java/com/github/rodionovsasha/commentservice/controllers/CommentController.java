package com.github.rodionovsasha.commentservice.controllers;

import com.github.rodionovsasha.commentservice.entities.Comment;
import com.github.rodionovsasha.commentservice.exceptions.CommentNotFoundException;
import com.github.rodionovsasha.commentservice.services.CommentService;
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
public class CommentController {
    private static final String COMMENT_BASE_PATH = "/comment";
    private final CommentService service;

    @GetMapping(value = COMMENT_BASE_PATH + "/{id}", produces = APPLICATION_JSON_VALUE)
    public Comment getComment(@PathVariable final long id) throws CommentNotFoundException {
        return service.getCommentById(id);
    }

    @PostMapping(value = COMMENT_BASE_PATH, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Comment> addComment(@Valid @RequestBody Comment comment) {
        return new ResponseEntity<>(service.addComment(comment), HttpStatus.CREATED);
    }

    @PutMapping(value = COMMENT_BASE_PATH, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity updateComment(@Valid @RequestBody Comment comment) throws CommentNotFoundException {
        return ResponseEntity.ok(service.updateComment(comment));
    }

    @DeleteMapping(COMMENT_BASE_PATH + "/{id}")
    public ResponseEntity deleteComment(@PathVariable final long id) {
        service.deleteComment(id);
        return ResponseEntity.noContent().build();
    }
}
