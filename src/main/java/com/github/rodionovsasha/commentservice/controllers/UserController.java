package com.github.rodionovsasha.commentservice.controllers;

import com.github.rodionovsasha.commentservice.entities.User;
import com.github.rodionovsasha.commentservice.services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService service;

    @GetMapping("/{id}")
    public User getActiveUser(@PathVariable int id) {
        return service.getActiveUser(id);
    }

    @ResponseStatus(CREATED)
    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        return service.create(user.getName(), user.getAge());
    }

    @PutMapping("/name")
    public void updateName(@Valid @RequestBody User user) {
        service.updateName(user.getId(), user.getName());
    }

    @PutMapping("/age")
    public void updateAge(@RequestBody User user) {
        service.updateAge(user.getId(), user.getAge());
    }

    @GetMapping("/{id}/deactivate")
    public void deactivate(@PathVariable int id) {
        service.deactivate(id);
    }

    @GetMapping("/{id}/activate")
    public void activate(@PathVariable int id) {
        service.activate(id);
    }
}
