package com.github.rodionovsasha.commentservice.exceptions;

public final class InactiveUserException extends AccessException {
    private InactiveUserException(String message) {
        super(message);
    }

    public static InactiveUserException forId(long id) {
        return new InactiveUserException("The user with id '%d' is not active".formatted(id));
    }
}
