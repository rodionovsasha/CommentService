package com.github.rodionovsasha.commentservice.exceptions;

public class UserNotFoundException extends Exception {
    public UserNotFoundException(final String message) {
        super(message);
    }
}
