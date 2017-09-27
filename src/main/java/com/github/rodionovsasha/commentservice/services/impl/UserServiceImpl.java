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

import java.util.function.Consumer;

@AllArgsConstructor
@Service @Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public User create(String name, int age) {
        return repository.save(new User(name, age));
    }

    @Override
    public void checkUserActive(long userId) {
        getActiveUser(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public User getActiveUser(long userId) {
        val user = getById(userId);
        if (!user.isActive()) {
            throw InactiveUserException.forId(userId);
        }
        return user;
    }

    @Override
    public void updateName(long id, String name) {
        update(id, user -> user.setName(name));
    }

    @Override
    public void updateAge(long id, int age) {
        update(id, user -> user.setAge(age));
    }

    @Override
    public void deactivate(long id) {
        update(id, user -> user.setActive(false));
    }

    @Override
    public void activate(long id) {
        val user = getById(id);
        user.setActive(true);
        repository.save(user);
    }

    private void update(long id, Consumer<User> consumer) {
        val user = getActiveUser(id);
        consumer.accept(user);
        repository.save(user);
    }

    private User getById(long id) {
        return repository.findOne(id).orElseThrow(() -> UserNotFoundException.forId(id));
    }
}
