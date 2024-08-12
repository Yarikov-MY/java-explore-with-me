package ru.practicum.explorewithme.ewmmainservice.event.model;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class UserEventsParams {
    private String text;
    private List<Integer> categories;
    private Boolean onlyAvailable;
    private Boolean paid;
    private SortParam sort;
    private LocalDateTime startLocal;
    private LocalDateTime endLocal;
    private int from;
    private int size = 10;

    public Specification<Event> getEventsSpecification() {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (text != null) {
                predicates.add(
                        criteriaBuilder.or(
                                criteriaBuilder.like(criteriaBuilder.lower(root.get("annotation")), "%" + text.toLowerCase() + "%"),
                                criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + text.toLowerCase() + "%")
                        )
                );
            }
            if (categories != null) {
                predicates.add(root.get("category").get("id").in(categories));
            }
            if (onlyAvailable != null && onlyAvailable) {
                predicates.add(criteriaBuilder.or(
                        criteriaBuilder.equal(root.get("participantLimit"), 0),
                        criteriaBuilder.greaterThan(root.get("participantLimit"), root.get("confirmedRequests")))
                );
            }
            if (paid != null) {
                predicates.add(criteriaBuilder.equal(root.get("paid"), paid));
            }
            if (sort != null && sort.equals(SortParam.EVENT_DATE)) {
                query.orderBy(criteriaBuilder.desc(root.get("eventDate")));
            }
            if (sort != null && sort.equals(SortParam.VIEWS)) {
                query.orderBy(criteriaBuilder.desc(root.get("views")));
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
