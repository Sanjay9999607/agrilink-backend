package com.agrilink.exception;

import org.springframework.http.*;
import org.springframework.web.bind
    .MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>>
            handleRuntime(RuntimeException ex) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>>
            handleValidation(MethodArgumentNotValidException ex) {
        String errors = ex.getBindingResult()
            .getFieldErrors().stream()
            .map(fe -> fe.getField()
                + ": " + fe.getDefaultMessage())
            .collect(Collectors.joining(", "));
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(Map.of("error", errors));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>>
            handleGeneric(Exception ex) {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(Map.of("error",
                "Something went wrong: " + ex.getMessage()));
    }
}