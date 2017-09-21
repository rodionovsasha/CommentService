package com.github.rodionovsasha.commentservice.exceptions;

public final class UserNotFoundException extends ServiceException {
    private UserNotFoundException(String message) {
        super(message);
    }

    public static void withId(long id) {
        throw new UserNotFoundException(String.format("The user with id '%d' could not be found", id));
    }
}
