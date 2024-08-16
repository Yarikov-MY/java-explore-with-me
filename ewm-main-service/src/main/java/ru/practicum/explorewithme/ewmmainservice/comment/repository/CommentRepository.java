package ru.practicum.explorewithme.ewmmainservice.comment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.ewmmainservice.comment.model.Comment;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    Page<Comment> findAllByEventIdOrderByCreatedDesc(Integer eventId, Pageable pageable);

    Optional<Comment> findByIdAndAuthorId(Integer id, Integer authorId);
}
