package com.github.rodionovsasha.commentservice.controllers;

import com.github.rodionovsasha.commentservice.entities.User;
import com.github.rodionovsasha.commentservice.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.github.rodionovsasha.commentservice.Application.API_BASE_URL;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@AllArgsConstructor
@RequestMapping(API_BASE_URL + "/user")
public class UserController {
    private final UserService service;

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public User getActiveUser(@PathVariable long id) {
        return service.getActiveUser(id);
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        return new ResponseEntity<>(service.create(user.getName(), user.getAge()), HttpStatus.CREATED);
    }
}
