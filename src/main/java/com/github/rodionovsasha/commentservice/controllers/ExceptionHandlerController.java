package com.github.rodionovsasha.commentservice.controllers;

import com.github.rodionovsasha.commentservice.dto.MessageResponse;
import com.github.rodionovsasha.commentservice.exceptions.TopicNotFoundException;
import com.github.rodionovsasha.commentservice.exceptions.UserNotFoundException;
import lombok.val;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ExceptionHandlerController {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<MessageResponse> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        val bindingResult = e.getBindingResult();
        val errors = new StringBuilder();
        bindingResult.getFieldErrors().forEach(
                error -> errors.append(error.getField())
                        .append(": ")
                        .append(error.getDefaultMessage())
                        .append(".")
        );

        return handleException(HttpStatus.BAD_REQUEST, errors.toString());
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    @ResponseBody
    public ResponseEntity<MessageResponse> emptyResultHandler() {
        return handleException(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.getReasonPhrase());
    }

    @ExceptionHandler({UserNotFoundException.class, TopicNotFoundException.class})
    @ResponseBody
    public ResponseEntity<MessageResponse> notFoundHandler(Exception e) {
        return handleException(HttpStatus.NOT_FOUND, e.getMessage());
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
