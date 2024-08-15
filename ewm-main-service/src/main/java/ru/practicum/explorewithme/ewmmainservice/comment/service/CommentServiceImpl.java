package ru.practicum.explorewithme.ewmmainservice.comment.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.ewmmainservice.comment.exception.CommentConflictException;
import ru.practicum.explorewithme.ewmmainservice.comment.exception.CommentNotFoundException;
import ru.practicum.explorewithme.ewmmainservice.comment.model.Comment;
import ru.practicum.explorewithme.ewmmainservice.comment.repository.CommentRepository;
import ru.practicum.explorewithme.ewmmainservice.event.exception.EventNotFoundException;
import ru.practicum.explorewithme.ewmmainservice.event.exception.InvalidEventStateException;
import ru.practicum.explorewithme.ewmmainservice.event.model.Event;
import ru.practicum.explorewithme.ewmmainservice.event.model.EventState;
import ru.practicum.explorewithme.ewmmainservice.event.repository.EventRepository;
import ru.practicum.explorewithme.ewmmainservice.request.model.Request;
import ru.practicum.explorewithme.ewmmainservice.request.model.RequestStatus;
import ru.practicum.explorewithme.ewmmainservice.request.repository.RequestRepository;
import ru.practicum.explorewithme.ewmmainservice.user.exception.UserNotFoundException;
import ru.practicum.explorewithme.ewmmainservice.user.model.User;
import ru.practicum.explorewithme.ewmmainservice.user.repository.UserRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;


    @Value("${time-to-edit-seconds}")
    private int timeToEditSeconds;

    public CommentServiceImpl(CommentRepository commentRepository, EventRepository eventRepository, RequestRepository requestRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.eventRepository = eventRepository;
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public Comment addComment(Integer eventId, Integer userId, Comment comment) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId, userId));
        if (Objects.equals(event.getInitiator().getId(), user.getId())) {
            throw new InvalidEventStateException("Пользователь не может комментировать событие, если он его инициатор!");
        }
        if (event.getState() != EventState.PUBLISHED) {
            throw new InvalidEventStateException("Событие должно быть в статусе - " + EventState.PUBLISHED + "!");
        }
        Optional<Request> request = requestRepository.findByEventIdAndRequesterId(eventId, userId);
        if (request.isEmpty() || request.get().getStatus() != RequestStatus.CONFIRMED) {
            throw new InvalidEventStateException("Пользователь(id=" + userId + ") не участвовал в событии(id=" + eventId + ")");
        }
        comment.setEvent(event);
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());
        return commentRepository.save(comment);
    }

    @Override
    @Transactional
    public Comment editComment(Integer commentId, Integer userId, Comment comment) {
        Comment targetComment = commentRepository.findByIdAndAuthorId(commentId, userId).orElseThrow(() -> new CommentNotFoundException(commentId, userId));
        LocalDateTime edited = LocalDateTime.now();
        if (targetComment.getEdited() == null || Duration.between(targetComment.getEdited(), edited).getSeconds() > timeToEditSeconds) {
            targetComment.setCommentText(comment.getCommentText());
            targetComment.setEdited(edited);
        } else {
            throw new CommentConflictException("Время для редактирования(" + timeToEditSeconds + ") истекло!");
        }

        return targetComment;
    }

    @Override
    @Transactional
    public void deleteComment(Integer commentId) {
        if (commentRepository.existsById(commentId)) {
            commentRepository.deleteById(commentId);
        } else {
            throw new CommentNotFoundException(commentId);
        }
    }

    @Override
    @Transactional
    public void deleteComment(Integer commentId, Integer userId) {
        Comment comment = commentRepository.findByIdAndAuthorId(commentId, userId).orElseThrow(() -> new CommentNotFoundException(commentId, userId));
        commentRepository.delete(comment);
    }

    @Override
    public List<Comment> getEventComments(Integer eventId, Integer from, Integer size) {
        return commentRepository.findAllByEventIdOrderByCreatedDesc(eventId, PageRequest.of(from / size, size)).getContent();
    }
}
