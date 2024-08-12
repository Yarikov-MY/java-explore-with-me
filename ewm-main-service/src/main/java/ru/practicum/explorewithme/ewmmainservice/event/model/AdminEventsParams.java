package ru.practicum.explorewithme.ewmmainservice.event.model;

import jakarta.persistence.criteria.Predicate;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class AdminEventsParams {
    private List<EventState> states;
    private List<Integer> categories;
    private List<Integer> users;
    private LocalDateTime startLocal;
    private LocalDateTime endLocal;
    private int from;
    private int size = 10;

    public Specification<Event> getEventsSpecification() {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (states != null) {
                predicates.add(root.get("state").in(states));
            }
            if (categories != null) {
                predicates.add(root.get("category").get("id").in(categories));
            }
            if (users != null) {
                predicates.add(root.get("initiator").get("id").in(users));
            }
            if (startLocal != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("eventDate"), startLocal));
            }
            if (endLocal != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("eventDate"), endLocal));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
