package com.github.rodionovsasha.commentservice.exceptions;

public final class ArchivedTopicException extends ServiceException {
    private ArchivedTopicException(String message) {
        super(message);
    }

    public static ArchivedTopicException forId(long id) {
        return new ArchivedTopicException(
                String.format("The topic with id '%d' is archived", id)
        );
    }
}
