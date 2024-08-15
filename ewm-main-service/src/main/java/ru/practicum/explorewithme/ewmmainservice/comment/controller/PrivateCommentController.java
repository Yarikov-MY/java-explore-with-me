package ru.practicum.explorewithme.ewmmainservice.comment.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.ewmmainservice.comment.dto.CommentDto;
import ru.practicum.explorewithme.ewmmainservice.comment.dto.CommentShortDto;
import ru.practicum.explorewithme.ewmmainservice.comment.mapper.CommentMapper;
import ru.practicum.explorewithme.ewmmainservice.comment.model.Comment;
import ru.practicum.explorewithme.ewmmainservice.comment.service.CommentService;


@RestController
@RequestMapping("/users/{userId}/comments")
public class PrivateCommentController {
    private final CommentService commentService;

    public PrivateCommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public CommentDto createComment(
            @RequestParam Integer eventId,
            @PathVariable Integer userId,
            @Valid @RequestBody CommentShortDto commentShortDto
    ) {
        Comment createdComment = commentService.addComment(eventId, userId, CommentMapper.toComment(commentShortDto));
        return CommentMapper.toCommentDto(createdComment);
    }

    @PatchMapping("/{commentId}")
    public CommentDto editComment(
            @PathVariable Integer commentId, @PathVariable Integer userId,
            @Valid @RequestBody CommentShortDto commentShortDto
    ) {
        Comment editedComment = commentService.editComment(commentId, userId, CommentMapper.toComment(commentShortDto));
        return CommentMapper.toCommentDto(editedComment);
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable Integer commentId, @PathVariable Integer userId) {
        commentService.deleteComment(commentId, userId);
    }
}
