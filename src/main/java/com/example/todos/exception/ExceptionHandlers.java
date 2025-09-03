package com.example.todos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class ExceptionHandlers {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponses> handleException(Exception ex) {
        return buildResponseEntity(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ExceptionResponses> handleResponseStatusException(ResponseStatusException ex) {
        return buildResponseEntity(ex, HttpStatus.valueOf(ex.getStatusCode().value()));
    }

    private ResponseEntity<ExceptionResponses> buildResponseEntity(Exception exc, HttpStatus status) {
        ExceptionResponses exceptionResponses = new ExceptionResponses(status.value(), exc.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(exceptionResponses, status);
    }
}
