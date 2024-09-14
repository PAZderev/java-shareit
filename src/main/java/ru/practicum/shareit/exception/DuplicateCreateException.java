package ru.practicum.shareit.exception;

public class DuplicateCreateException extends RuntimeException {
    public DuplicateCreateException(String message) {
        super(message);
    }
}
