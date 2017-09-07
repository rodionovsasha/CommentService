package com.github.rodionovsasha.commentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
public class MessageResponse implements Serializable {
    private final int responseCode;
    private final String statusMessage;
}
