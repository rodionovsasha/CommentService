package com.github.rodionovsasha.commentservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public final class InactiveUserException extends ServiceException {
    private InactiveUserException(String message) {
        super(message);
    }

    public static InactiveUserException forId(long id) {
        return new InactiveUserException(String.format("The user with id '%d' is not active", id));
    }
}
