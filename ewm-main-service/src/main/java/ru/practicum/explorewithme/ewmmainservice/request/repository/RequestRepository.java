package ru.practicum.explorewithme.ewmmainservice.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.ewmmainservice.request.model.Request;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Integer> {
    Optional<Request> findByEventIdAndRequesterId(int eventId, int userId);

    Optional<Request> findByIdAndRequesterId(int requestId, int userId);

    List<Request> findAllByRequesterId(int userId);

    List<Request> findAllByEventId(int eventId);
}
