package com.github.rodionovsasha.commentservice.exceptions;

class ServiceException extends RuntimeException {
    ServiceException(String message) {
        super(message);
    }
}
