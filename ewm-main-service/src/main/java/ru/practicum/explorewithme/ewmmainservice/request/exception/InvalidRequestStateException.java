package ru.practicum.explorewithme.ewmmainservice.request.exception;


public class InvalidRequestStateException extends RuntimeException {
    public InvalidRequestStateException(String message) {
        super(message);
    }
}
