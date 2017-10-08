package com.github.rodionovsasha.commentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
public class MessageResponse implements Serializable {
    private static final long serialVersionUID = 28116440335967L;

    private final int code;
    private final String message;
}
