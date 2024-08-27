package ru.practicum.shareit.utils;


import com.sun.jdi.request.DuplicateRequestException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exception.AccessRightException;
import ru.practicum.shareit.exception.NotFoundException;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException e, HttpServletRequest request) {
        log.error("NotFoundException: {}", e.getMessage());
        ErrorResponse errorResponse = ErrorResponse.builder()
                .error(e.getMessage() + "; ")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccessRightException.class)
    public ResponseEntity<ErrorResponse> handleAccessRightException(AccessRightException e, HttpServletRequest request) {
        log.error("AccessRightException: {}", e.getMessage());
        ErrorResponse errorResponse = ErrorResponse.builder()
                .error(e.getMessage() + "; ")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.FORBIDDEN.value())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(DuplicateRequestException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateRequestException(DuplicateRequestException e, HttpServletRequest request) {
        log.error("DuplicateRequestException: {}", e.getMessage());
        ErrorResponse errorResponse = ErrorResponse.builder()
                .error(e.getMessage() + "; ")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CONFLICT.value())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        log.error("MethodArgumentNotValidException: {}", ex.getMessage());
        StringBuilder errors = new StringBuilder();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            errors.append(error.getDefaultMessage()).append("; ");
            log.error("Validation error {}", error.getDefaultMessage());
        });
        ErrorResponse errorResponse = ErrorResponse.builder()
                .error(errors.toString())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}
