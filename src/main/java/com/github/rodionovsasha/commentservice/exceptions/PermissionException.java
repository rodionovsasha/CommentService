package com.github.rodionovsasha.commentservice.exceptions;

public final class PermissionException extends ServiceException {
    private PermissionException(String message) {
        super(message);
    }

    public static PermissionException forId(long id) {
        return new PermissionException(String.format("Non-topic owner with id '%d' is trying to delete the topic", id));
    }
}
