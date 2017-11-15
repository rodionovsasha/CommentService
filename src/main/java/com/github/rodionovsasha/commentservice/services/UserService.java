package com.github.rodionovsasha.commentservice.services;

import com.github.rodionovsasha.commentservice.entities.User;

public interface UserService {
    User create(String name, int age);

    void checkUserActive(int id);

    User getActiveUser(int id);

    void updateName(int id, String name);

    void updateAge(int id, int age);

    void deactivate(int id);

    void activate(int id);
}
