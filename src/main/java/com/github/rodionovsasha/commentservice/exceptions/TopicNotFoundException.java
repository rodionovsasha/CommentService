package com.github.rodionovsasha.commentservice.exceptions;

public final class TopicNotFoundException extends NotFoundException {
    private TopicNotFoundException(String message) {
        super(message);
    }

    public static TopicNotFoundException forId(long id) {
        return new TopicNotFoundException("The topic with id '%d' could not be found".formatted(id));
    }
}
