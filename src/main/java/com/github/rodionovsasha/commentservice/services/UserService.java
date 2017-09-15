package com.github.rodionovsasha.commentservice.services;

import com.github.rodionovsasha.commentservice.entities.User;
import com.github.rodionovsasha.commentservice.exceptions.InactiveUserException;
import com.github.rodionovsasha.commentservice.exceptions.UserNotFoundException;

public interface UserService {
    User create(String name, int age);

    User getById(long id) throws UserNotFoundException;

    User getActive(long userId) throws InactiveUserException, UserNotFoundException;

    void updateName(long id, String name) throws UserNotFoundException;

    void updateAge(long id, int age) throws UserNotFoundException;

    void deactivate(long id) throws UserNotFoundException;

    void delete(long id) throws UserNotFoundException;
}
