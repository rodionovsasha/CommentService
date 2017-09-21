package com.github.rodionovsasha.commentservice.exceptions;

public class UserNotFoundException extends ServiceException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
