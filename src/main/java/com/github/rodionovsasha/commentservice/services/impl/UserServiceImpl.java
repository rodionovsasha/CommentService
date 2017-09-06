package com.github.rodionovsasha.commentservice.services.impl;

import com.github.rodionovsasha.commentservice.entities.User;
import com.github.rodionovsasha.commentservice.exceptions.UserNotFoundException;
import com.github.rodionovsasha.commentservice.repositories.UserRepository;
import com.github.rodionovsasha.commentservice.services.UserService;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public User addUser(User user) {
        return userRepository.saveAndFlush(user);
    }

    @Override
    @Transactional
    public User updateUser(User user) throws UserNotFoundException {
        val currentUser = getUserById(user.getId());
        currentUser.setName(user.getName());
        currentUser.setAge(user.getAge());
        currentUser.setEnabled(user.isEnabled());
        return userRepository.saveAndFlush(currentUser);
    }

    @Override
    public void deleteUser(long id) {
        userRepository.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(long id) throws UserNotFoundException {
        val user = userRepository.findOne(id);
        if (user == null) {
            throw new UserNotFoundException("User with id '" + id + "' not found");
        }
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
}
