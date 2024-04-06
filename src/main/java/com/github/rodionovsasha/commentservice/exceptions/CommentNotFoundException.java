package com.github.rodionovsasha.commentservice.exceptions;

public final class CommentNotFoundException extends NotFoundException {
    private CommentNotFoundException(String message) {
        super(message);
    }

    public static CommentNotFoundException forId(long id) {
        return new CommentNotFoundException("The comment with id '%d' could not be found".formatted(id));
    }
}
