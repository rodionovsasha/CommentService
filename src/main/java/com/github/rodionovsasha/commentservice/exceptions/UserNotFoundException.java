package com.github.rodionovsasha.commentservice.exceptions;

public final class UserNotFoundException extends NotFoundException {
    private UserNotFoundException(String message) {
        super(message);
    }

    public static UserNotFoundException forId(long id) {
        return new UserNotFoundException("The user with id '%d' could not be found".formatted(id));
    }
}
