package com.github.rodionovsasha.commentservice.controllers;

import com.github.rodionovsasha.commentservice.entities.User;
import com.github.rodionovsasha.commentservice.exceptions.UserNotFoundException;
import com.github.rodionovsasha.commentservice.services.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.github.rodionovsasha.commentservice.AppConfig.API_BASE_URL;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@Slf4j
@RestController
@RequestMapping(API_BASE_URL)
public class UserController {
    private static final String USER_BASE_PATH = "/user";
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation("Get all users")
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public List<User> getAllUsers() {
        return userService.findAllUsers();
    }


    @ApiOperation("Get user by ID")
    @GetMapping(value = USER_BASE_PATH + "/{id}", produces = APPLICATION_JSON_VALUE)
    public User getUser(@PathVariable final long id) throws UserNotFoundException {
        return userService.getUserById(id);
    }

    @ApiOperation("Add user")
    @PostMapping(value = USER_BASE_PATH, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<User> addUser(@Valid @RequestBody User user) {
        val newUser = userService.addUser(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @ApiOperation("Update user")
    @PutMapping(value = USER_BASE_PATH, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity updateUser(@Valid @RequestBody User user) throws UserNotFoundException {
        val updatedUser = userService.updateUser(user);
        return ResponseEntity.ok(updatedUser);
    }

    @ApiOperation("Delete user")
    @DeleteMapping(USER_BASE_PATH + "/{id}")
    public ResponseEntity deleteUser(@PathVariable final long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}