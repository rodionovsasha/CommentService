package com.github.rodionovsasha.commentservice.controllers;

import com.github.rodionovsasha.commentservice.dto.MessageResponse;
import com.github.rodionovsasha.commentservice.exceptions.*;
import lombok.val;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionHandlerController {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<MessageResponse> fieldErrorsHandler(MethodArgumentNotValidException e) {
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

    @ExceptionHandler({UserNotFoundException.class,
            TopicNotFoundException.class,
            CommentNotFoundException.class,
            EmptyResultDataAccessException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ResponseEntity<MessageResponse> notFoundHandler(Exception e) {
        return handleException(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(PermissionException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ResponseEntity<MessageResponse> permissionHandler(Exception e) {
        return handleException(HttpStatus.FORBIDDEN, e.getMessage());
    }

    @ExceptionHandler(InactiveUserException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ResponseEntity<MessageResponse> inactiveHandler(Exception e) {
        return handleException(HttpStatus.UNAUTHORIZED, e.getMessage());
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
