package com.github.rodionovsasha.commentservice.controllers;

import com.github.rodionovsasha.commentservice.entities.Comment;
import com.github.rodionovsasha.commentservice.exceptions.CommentNotFoundException;
import com.github.rodionovsasha.commentservice.services.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.github.rodionovsasha.commentservice.Application.API_BASE_URL;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@AllArgsConstructor
@RequestMapping(API_BASE_URL + "/comment")
public class CommentController {
    private final CommentService service;

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public Comment getComment(@PathVariable final long id) throws CommentNotFoundException {
        return service.getById(id);
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Comment> addComment(@Valid @RequestBody Comment comment) {
        return new ResponseEntity<>(service.add(comment), HttpStatus.CREATED);
    }

    @PutMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity updateComment(@Valid @RequestBody Comment comment) throws CommentNotFoundException {
        return ResponseEntity.ok(service.update(comment));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteComment(@PathVariable final long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
