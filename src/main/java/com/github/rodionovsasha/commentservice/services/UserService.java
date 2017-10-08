package com.github.rodionovsasha.commentservice.services;

import com.github.rodionovsasha.commentservice.entities.User;

public interface UserService {
    User create(String name, int age);

    void checkUserActive(long id);

    User getActiveUser(long id);

    void updateName(long id, String name);

    void updateAge(long id, int age);

    void deactivate(long id);

    void activate(long id);
}
