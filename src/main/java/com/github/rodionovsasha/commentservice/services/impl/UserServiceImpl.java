package com.github.rodionovsasha.commentservice.services.impl;

import com.github.rodionovsasha.commentservice.entities.User;
import com.github.rodionovsasha.commentservice.exceptions.InactiveUserException;
import com.github.rodionovsasha.commentservice.exceptions.UserNotFoundException;
import com.github.rodionovsasha.commentservice.repositories.UserRepository;
import com.github.rodionovsasha.commentservice.services.UserService;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service @Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public User create(String name, int age) {
        return repository.saveAndFlush(new User(name, age));
    }

    @Override
    @Transactional(readOnly = true)
    public User getById(long id) throws UserNotFoundException {
        val user = repository.findOne(id);
        if (user == null) {
            throw new UserNotFoundException(String.format("User with id '%d' not found", id));
        }
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public User getActive(long userId) throws InactiveUserException, UserNotFoundException {
        val user = getById(userId);
        if (!user.isEnabled()) {
            throw new InactiveUserException(String.format("User with userId '%d' is not active", userId));
        }
        return user;
    }

    @Override
    public void updateName(long id, String name) throws UserNotFoundException, InactiveUserException {
        val user = getActive(id);
        user.setName(name);
        repository.saveAndFlush(user);
    }

    @Override
    public void updateAge(long id, int age) throws UserNotFoundException, InactiveUserException {
        val user = getActive(id);
        user.setAge(age);
        repository.saveAndFlush(user);
    }

    @Override
    public void deactivate(long id) throws UserNotFoundException {
        val user = getById(id);
        user.setEnabled(false);
        repository.saveAndFlush(user);
    }

    @Override
    public void delete(long id) {
        repository.delete(id);
    }
}
