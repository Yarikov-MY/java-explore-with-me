package ru.practicum.explorewithme.ewmmainservice.comment.exception;

public class CommentConflictException extends RuntimeException {
    public CommentConflictException(String message) {
        super(message);
    }
}
