package ru.practicum.explorewithme.ewmmainservice.event.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.practicum.explorewithme.ewmmainservice.category.model.Category;
import ru.practicum.explorewithme.ewmmainservice.user.model.User;

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
