package com.example.todos.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class ExceptionResponses{
    private int status;
    private String message;
    private long timeStamp;
}


