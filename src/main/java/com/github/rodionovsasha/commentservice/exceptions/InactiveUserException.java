package com.github.rodionovsasha.commentservice.exceptions;

public class InactiveUserException extends Exception {
    public InactiveUserException(final String message) {
        super(message);
    }
}
