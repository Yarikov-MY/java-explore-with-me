package ru.practicum.explorewithme.ewmmainservice.event.exception;


public class InvalidEventStateException extends RuntimeException {
    public InvalidEventStateException(String message) {
        super(message);
    }
}
