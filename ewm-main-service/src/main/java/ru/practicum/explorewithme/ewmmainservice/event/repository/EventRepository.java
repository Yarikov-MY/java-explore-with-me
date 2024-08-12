package ru.practicum.explorewithme.ewmmainservice.event.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.practicum.explorewithme.ewmmainservice.event.model.Event;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Integer>, JpaSpecificationExecutor<Event> {
    Page<Event> findAll(Specification<Event> specification, Pageable pageable);

    List<Event> findAllByIdIn(List<Integer> ids);

    Page<Event> findAllByInitiatorId(Integer userId, Pageable pageable);

    Optional<Event> findEventByIdAndInitiatorId(Integer eventId, Integer userId);

    Boolean existsByCategoryId(Integer id);
}
