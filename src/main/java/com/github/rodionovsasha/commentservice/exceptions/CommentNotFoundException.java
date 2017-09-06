package com.github.rodionovsasha.commentservice.exceptions;

public class CommentNotFoundException extends Exception {
    public CommentNotFoundException(final String message) {
        super(message);
    }
}
