package com.github.rodionovsasha.commentservice.controllers;

import com.github.rodionovsasha.commentservice.entities.User;
import com.github.rodionovsasha.commentservice.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.github.rodionovsasha.commentservice.Application.API_BASE_URL;

@RestController
@AllArgsConstructor
@RequestMapping(API_BASE_URL + "/user")
public class UserController {
    private final UserService service;

    @GetMapping("/{id}")
    public User getActiveUser(@PathVariable long id) {
        return service.getActiveUser(id);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        return new ResponseEntity<>(service.create(user.getName(), user.getAge()), HttpStatus.CREATED);
    }

    @PutMapping("/name")
    public void updateName(@Valid @RequestBody User user) {
        service.updateName(user.getId(), user.getName());
    }

    @PutMapping("/age")
    public void updateAge(@RequestBody User user) {
        service.updateAge(user.getId(), user.getAge());
    }
}
