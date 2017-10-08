package com.github.rodionovsasha.commentservice.controllers;

import com.github.rodionovsasha.commentservice.dto.MessageResponse;
import com.github.rodionovsasha.commentservice.exceptions.InactiveUserException;
import com.github.rodionovsasha.commentservice.exceptions.UserNotFoundException;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ExceptionHandlerController {
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseBody
    public ResponseEntity<MessageResponse> notFoundHandler(Exception e) {
        return handleException(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(InactiveUserException.class)
    @ResponseBody
    public ResponseEntity<MessageResponse> inactiveHandler(Exception e) {
        return handleException(HttpStatus.FORBIDDEN, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<MessageResponse> defaultHandler(Exception e) {
        return handleException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    private ResponseEntity<MessageResponse> handleException(HttpStatus httpStatus, String message) {
        val messageResponse = new MessageResponse(httpStatus.value(), message);
        return new ResponseEntity<>(messageResponse, httpStatus);
    }
}
