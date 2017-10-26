package com.github.rodionovsasha.commentservice.exceptions;

public final class CommentAccessException extends AccessException {
    private CommentAccessException(String message) {
        super(message);
    }

    public static CommentAccessException forId(long id) {
        return new CommentAccessException(
                String.format("Non-comment owner with id '%d' is trying to update the comment", id)
        );
    }
}
