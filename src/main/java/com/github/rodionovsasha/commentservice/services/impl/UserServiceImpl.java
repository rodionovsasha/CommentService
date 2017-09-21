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
    @Transactional(readOnly = true)
    public User getById(long id) {
        val user = repository.findOne(id);
        if (user == null) {
            UserNotFoundException.withId(id);
        }
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public User getActiveUser(long userId) {
        val user = getById(userId);
        if (!user.isEnabled()) {
            InactiveUserException.withId(userId);
        }
        return user;
    }

    @Override
    public void updateName(long id, String name) {
        change(id, user -> user.setName(name));
    }

    @Override
    public void updateAge(long id, int age) {
        change(id, user -> user.setAge(age));
    }

    @Override
    public void deactivate(long id) {
        change(id, user -> user.setEnabled(false));
    }

    @Override
    public void delete(long id) {
        repository.delete(id);
    }

    private void change(long id, Consumer<User> consumer) {
        val user = getActiveUser(id);
        consumer.accept(user);
        repository.save(user);
    }
}
