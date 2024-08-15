package ru.practicum.explorewithme.ewmmainservice.comment.mapper;

import lombok.NonNull;
import ru.practicum.explorewithme.ewmmainservice.comment.dto.CommentDto;
import ru.practicum.explorewithme.ewmmainservice.comment.dto.CommentShortDto;
import ru.practicum.explorewithme.ewmmainservice.comment.model.Comment;

public class CommentMapper {
    public static Comment toComment(@NonNull CommentShortDto commentShortDto) {
        return Comment.builder()
                .commentText(commentShortDto.getCommentText())
                .build();
    }

    public static CommentDto toCommentDto(@NonNull Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .eventId(comment.getEvent().getId())
                .authorId(comment.getAuthor().getId())
                .authorName(comment.getAuthor().getName())
                .commentText(comment.getCommentText())
                .created(comment.getCreated())
                .edited(comment.getEdited())
                .build();
    }
}
