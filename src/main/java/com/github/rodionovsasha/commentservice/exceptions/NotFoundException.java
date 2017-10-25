package com.github.rodionovsasha.commentservice.exceptions;

public class NotFoundException extends RuntimeException {
    NotFoundException(String message) {
        super(message);
    }
}
