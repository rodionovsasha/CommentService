package com.github.rodionovsasha.commentservice.exceptions;

public final class UserNotFoundException extends ServiceException {
    private UserNotFoundException(String message) {
        super(message);
    }

    public static UserNotFoundException withId(long id) {
        return new UserNotFoundException(String.format("The user with id '%d' could not be found", id));
    }
}
