package com.github.rodionovsasha.commentservice.exceptions;

public final class TopicAccessException extends ServiceException {
    private TopicAccessException(String message) {
        super(message);
    }

    public static TopicAccessException forId(long id) {
        return new TopicAccessException(
                String.format("Non-topic owner with id '%d' is trying to archive the topic", id)
        );
    }
}
