package com.github.rodionovsasha.commentservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public final class UserNotFoundException extends ServiceException {
    private UserNotFoundException(String message) {
        super(message);
    }

    public static UserNotFoundException forId(long id) {
        return new UserNotFoundException(String.format("The user with id '%d' could not be found", id));
    }
}
