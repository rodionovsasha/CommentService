package com.github.rodionovsasha.commentservice.exceptions;

public class InactiveUserException extends ServiceException {
    public InactiveUserException(String message) {
        super(message);
    }
}
