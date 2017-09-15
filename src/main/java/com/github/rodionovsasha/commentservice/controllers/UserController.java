package com.github.rodionovsasha.commentservice.controllers;

import com.github.rodionovsasha.commentservice.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.github.rodionovsasha.commentservice.Application.API_BASE_URL;


@RestController
@AllArgsConstructor
@RequestMapping(API_BASE_URL + "/user")
public class UserController {
    private final UserService service;

/*    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public User getUser(@PathVariable final long id) throws UserNotFoundException {
        return service.getById(id);
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
    }*/
}
