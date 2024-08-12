package ru.practicum.explorewithme.ewmmainservice.request.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.ewmmainservice.event.model.Event;
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
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "requests")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @JoinColumn(name = "event_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Event event;
    @JoinColumn(name = "requester_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User requester;
    private LocalDateTime created;
    @Enumerated(EnumType.STRING)
    private RequestStatus status;

}
