package com.github.rodionovsasha.commentservice.exceptions;

public final class TopicAccessException extends AccessException {
    private TopicAccessException(String message) {
        super(message);
    }

    public static TopicAccessException forId(long id) {
        return new TopicAccessException("Non-topic owner with id '%d' is trying to archive the topic".formatted(id));
    }
}
