package com.github.rodionovsasha.commentservice.controllers;

import com.github.rodionovsasha.commentservice.entities.User;
import com.github.rodionovsasha.commentservice.exceptions.UserNotFoundException;
import com.github.rodionovsasha.commentservice.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.github.rodionovsasha.commentservice.Application.API_BASE_URL;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@RestController
@AllArgsConstructor
@RequestMapping(API_BASE_URL + "/user")
public class UserController {
    private final UserService service;

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public User getUser(@PathVariable final long id) throws UserNotFoundException {
        return service.getUserById(id);
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<User> addUser(@Valid @RequestBody User user) {
        return new ResponseEntity<>(service.addUser(user), HttpStatus.CREATED);
    }

    @PutMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity updateUser(@Valid @RequestBody User user) throws UserNotFoundException {
        return ResponseEntity.ok(service.updateUser(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUser(@PathVariable final long id) {
        service.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
