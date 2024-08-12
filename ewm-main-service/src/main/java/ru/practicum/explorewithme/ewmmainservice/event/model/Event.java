package ru.practicum.explorewithme.ewmmainservice.event.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.practicum.explorewithme.ewmmainservice.category.model.Category;
import ru.practicum.explorewithme.ewmmainservice.user.model.User;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String annotation;
    @JoinColumn(name = "category_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Category category;
    private Integer confirmedRequests;
    private String description;
    private LocalDateTime eventDate;
    @JoinColumn(name = "location_id")
    @OneToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private String title;
    @JoinColumn(name = "initiator")
    @OneToOne(fetch = FetchType.LAZY)
    private User initiator;
    private LocalDateTime createdOn;
    private LocalDateTime publishedOn;
    @Enumerated(EnumType.STRING)
    private EventState state;
    private Integer views;
}
