package com.github.rodionovsasha.commentservice.controllers;

import com.github.rodionovsasha.commentservice.dto.MessageResponse;
import com.github.rodionovsasha.commentservice.exceptions.AccessException;
import com.github.rodionovsasha.commentservice.exceptions.NotFoundException;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionHandlerController {
    @ExceptionHandler(NotFoundException.class)
    @ResponseBody @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<MessageResponse> notFoundHandler(Exception e) {
        return handleException(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(AccessException.class)
    @ResponseBody @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<MessageResponse> inactiveHandler(Exception e) {
        return handleException(HttpStatus.FORBIDDEN, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<MessageResponse> validationHandler(Exception e) {
        return handleException(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<MessageResponse> defaultHandler(Exception e) {
        return handleException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    private ResponseEntity<MessageResponse> handleException(HttpStatus httpStatus, String message) {
        val messageResponse = new MessageResponse(httpStatus.value(), message);
        return new ResponseEntity<>(messageResponse, httpStatus);
    }
}
