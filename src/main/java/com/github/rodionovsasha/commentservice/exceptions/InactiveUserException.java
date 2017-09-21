package com.github.rodionovsasha.commentservice.exceptions;

public final class InactiveUserException extends ServiceException {
    private InactiveUserException(String message) {
        super(message);
    }

    public static void withId(long id) {
        throw new InactiveUserException(String.format("The user with id '%d' is not active", id));
    }
}
