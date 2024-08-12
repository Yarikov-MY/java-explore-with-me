package ru.practicum.explorewithme.ewmmainservice.category.exception;

public class CategoryConflictException extends RuntimeException {
    public CategoryConflictException(String message) {
        super(message);
    }
}
