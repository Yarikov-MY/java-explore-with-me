package ru.practicum.explorewithme.ewmmainservice.comment.exception;

public class CommentNotFoundException extends RuntimeException {
    public CommentNotFoundException(int commentId, int userId) {
        super("Комментарий id=(" + commentId + ") не найден у пользователя id=(" + userId + ")");
    }

    public CommentNotFoundException(int commentId) {
        super("Комментарий(id=" + commentId + ") не найден");
    }
}
