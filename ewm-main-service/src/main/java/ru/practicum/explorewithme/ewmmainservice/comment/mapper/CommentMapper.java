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
        CommentDto.CommentDtoBuilder builder = CommentDto.builder()
                .id(comment.getId())
                .commentText(comment.getCommentText())
                .created(comment.getCreated())
                .edited(comment.getEdited());
        if (comment.getEvent() != null) {
            builder.eventId(comment.getEvent().getId());
        }
        if (comment.getAuthor() != null) {
            builder.authorId(comment.getAuthor().getId()).authorName(comment.getAuthor().getName());
        }
        return builder.build();
    }
}
