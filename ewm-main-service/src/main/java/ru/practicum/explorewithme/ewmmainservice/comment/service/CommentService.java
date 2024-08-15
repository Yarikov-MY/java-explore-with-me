package ru.practicum.explorewithme.ewmmainservice.comment.service;

import ru.practicum.explorewithme.ewmmainservice.comment.model.Comment;

import java.util.List;

public interface CommentService {
    Comment addComment(Integer eventId, Integer userId, Comment comment);

    Comment editComment(Integer commentId, Integer userId, Comment comment);

    void deleteComment(Integer commentId);

    void deleteComment(Integer commentId, Integer userId);

    List<Comment> getEventComments(Integer eventId, Integer from, Integer size);
}
