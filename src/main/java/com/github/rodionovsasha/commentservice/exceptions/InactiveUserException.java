package com.github.rodionovsasha.commentservice.exceptions;

public class InactiveUserException extends ServiceException {
    public InactiveUserException(final String message) {
        super(message);
    }
}
