package com.github.rodionovsasha.commentservice.exceptions;

class ServiceException extends RuntimeException {
    ServiceException(final String message) {
        super(message);
    }
}
