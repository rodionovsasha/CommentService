package com.github.rodionovsasha.commentservice.services;

import com.github.rodionovsasha.commentservice.entities.User;
import com.github.rodionovsasha.commentservice.exceptions.UserNotFoundException;

import java.util.List;

public interface UserService {
    User addUser(User user);
    User updateUser(User user) throws UserNotFoundException;
    void deleteUser(long id);
    User getUserById(long id) throws UserNotFoundException;
    List<User> findAllUsers();
}
