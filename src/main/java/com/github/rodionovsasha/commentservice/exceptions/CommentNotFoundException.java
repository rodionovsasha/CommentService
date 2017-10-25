package com.github.rodionovsasha.commentservice.exceptions;

public final class CommentNotFoundException extends NotFoundException {
    private CommentNotFoundException(String message) {
        super(message);
    }

    public static CommentNotFoundException forId(long id) {
        return new CommentNotFoundException(String.format("The comment with id '%d' could not be found", id));
    }
}
