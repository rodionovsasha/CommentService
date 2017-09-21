package com.github.rodionovsasha.commentservice.exceptions;

public class UserNotFoundException extends ServiceException {
    public UserNotFoundException(final String message) {
        super(message);
    }
}
