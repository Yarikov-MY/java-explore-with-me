package ru.practicum.explorewithme.ewmmainservice.event.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.ewmmainservice.category.exception.CategoryNotFoundException;
import ru.practicum.explorewithme.ewmmainservice.category.model.Category;
import ru.practicum.explorewithme.ewmmainservice.category.repository.CategoryRepository;
import ru.practicum.explorewithme.ewmmainservice.event.exception.EventNotFoundException;
import ru.practicum.explorewithme.ewmmainservice.event.exception.InvalidEventStateException;
import ru.practicum.explorewithme.ewmmainservice.event.model.AdminEventsParams;
import ru.practicum.explorewithme.ewmmainservice.event.model.Event;
import ru.practicum.explorewithme.ewmmainservice.event.model.EventState;
import ru.practicum.explorewithme.ewmmainservice.event.model.Location;
import ru.practicum.explorewithme.ewmmainservice.event.model.StateAction;
import ru.practicum.explorewithme.ewmmainservice.event.model.UserEventsParams;
import ru.practicum.explorewithme.ewmmainservice.event.repository.EventRepository;
import ru.practicum.explorewithme.ewmmainservice.event.repository.LocationRepository;
import ru.practicum.explorewithme.ewmmainservice.user.exception.UserNotFoundException;
import ru.practicum.explorewithme.ewmmainservice.user.model.User;
import ru.practicum.explorewithme.ewmmainservice.user.repository.UserRepository;
import ru.practicum.explorewithme.ewmstatsservice.client.EwmStatsClient;
import ru.practicum.explorewithme.ewmstatsservice.dto.EndpointHitDto;
import ru.practicum.explorewithme.ewmstatsservice.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService {
    private final EwmStatsClient ewmStatsClient;
    private final EventRepository eventRepository;
    private final LocationRepository locationRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public EventServiceImpl(EwmStatsClient ewmStatsClient, EventRepository eventRepository, LocationRepository locationRepository, UserRepository userRepository, CategoryRepository categoryRepository) {
        this.ewmStatsClient = ewmStatsClient;
        this.eventRepository = eventRepository;
        this.locationRepository = locationRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Event> getEventsByUserId(Integer userId, Integer from, Integer size) {
        return eventRepository.findAllByInitiatorId(userId, PageRequest.of(from / size, size)).getContent();
    }

    @Override
    public Event getUserEventById(Integer userId, Integer eventId) {
        return eventRepository.findEventByIdAndInitiatorId(eventId, userId).orElseThrow(() -> new EventNotFoundException(eventId, userId));
    }

    @Override
    @Transactional
    public Event addEvent(Integer userId, Event event) {
        User user = getUser(userId);
        Category category = getCategory(event.getCategory().getId());
        event.setInitiator(user);
        event.setCategory(category);
        event.setCreatedOn(LocalDateTime.now());
        event.setState(EventState.PENDING);
        event.setConfirmedRequests(0);
        event.setLocation(getLocationWithId(event.getLocation()));
        event.setViews(0);
        if (event.getPaid() == null)
            event.setPaid(false);
        if (event.getParticipantLimit() == null)
            event.setParticipantLimit(0);
        if (event.getRequestModeration() == null)
            event.setRequestModeration(true);
        return eventRepository.save(event);
    }

    @Override
    @Transactional
    public Event updateEvent(Integer userId, Integer eventId, StateAction stateAction, Event event) {
        User user = getUser(userId);
        Event currentEvent = eventRepository.findEventByIdAndInitiatorId(eventId, user.getId()).orElseThrow(() -> new EventNotFoundException(eventId, userId));
        if (currentEvent.getPublishedOn() != null) {
            throw new InvalidEventStateException("Можно изменить события только в состоянии PENDING/CANCELED");
        }
        if (event.getCategory() != null) {
            currentEvent.setCategory(getCategory(event.getCategory().getId()));
        }
        if (stateAction == StateAction.SEND_TO_REVIEW) {
            currentEvent.setState(EventState.PENDING);
        } else {
            currentEvent.setState(EventState.CANCELED);
        }
        return eventRepository.save(setDescriptionFields(event, currentEvent));
    }

    @Override
    public List<Event> getAllEvents(AdminEventsParams adminEventsParams) {
        Pageable pageable = PageRequest.of(adminEventsParams.getFrom() / adminEventsParams.getSize(), adminEventsParams.getSize());
        return eventRepository.findAll(adminEventsParams.getEventsSpecification(), pageable).getContent();
    }

    @Override
    @Transactional
    public Event updateEvent(Integer eventId, StateAction stateAction, Event event) {
        Event currentEvent = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
        if (event.getCategory() != null) {
            currentEvent.setCategory(getCategory(event.getCategory().getId()));
        }
        if (stateAction == StateAction.PUBLISH_EVENT) {
            if (currentEvent.getState() == EventState.PUBLISHED) {
                throw new InvalidEventStateException("Событие уже опубликовано");
            } else if (currentEvent.getState() == EventState.CANCELED) {
                throw new InvalidEventStateException("Событие отменено");
            } else {
                currentEvent.setState(EventState.PUBLISHED);
                currentEvent.setPublishedOn(LocalDateTime.now());
                currentEvent.setViews(0);
            }
        } else {
            if (currentEvent.getState() == EventState.PUBLISHED) {
                throw new InvalidEventStateException("Событие уже опубликовано и не может быть отменено");
            }
            currentEvent.setState(EventState.CANCELED);
        }
        return eventRepository.save(setDescriptionFields(event, currentEvent));
    }

    @Override
    @Transactional
    public List<Event> getAllEvents(UserEventsParams userEventsParams, String ip) {
        Pageable pageable = PageRequest.of(
                userEventsParams.getFrom() / userEventsParams.getSize(),
                userEventsParams.getSize()
        );
        List<Event> events = eventRepository.findAll(userEventsParams.getEventsSpecification(), pageable).getContent();
        saveHit(ip, "/events");
        Map<Integer, Event> publishedEvents = events.stream()
                .filter(event -> event.getState() == EventState.PUBLISHED)
                .collect(Collectors.toMap(Event::getId, Function.identity()));
        if (!publishedEvents.isEmpty()) {
            Map<Integer, ViewStatsDto> stats = ewmStatsClient.getViewStats(
                    publishedEvents.values().stream().map(Event::getPublishedOn).sorted().findFirst().get(),
                    LocalDateTime.now(),
                    publishedEvents.keySet().stream().map(id -> "/events" + "/" + id).collect(Collectors.toList()),
                    true
            ).stream().collect(Collectors.toMap(
                    statDTO -> {
                        String[] splitUri = statDTO.getUri().split("/");
                        return Integer.valueOf(splitUri[splitUri.length - 1]);
                    }, Function.identity()
            ));
            publishedEvents.forEach((id, event) -> {
                saveHit(ip, "/events" + "/" + id);
                if (!stats.containsKey(id)) {
                    event.setViews(event.getViews() + 1);
                }
            });
        }
        return events;
    }

    @Override
    @Transactional
    public Event getEventById(Integer eventId, String ip) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
        if (event.getState() != EventState.PUBLISHED) {
            throw new EventNotFoundException("Событие не опубликовано");
        }
        String url = "/events" + "/" + eventId;
        List<ViewStatsDto> stats = ewmStatsClient.getViewStats(event.getPublishedOn(), LocalDateTime.now(), List.of(url), true);
        saveHit(ip, url);
        if (stats.isEmpty()) {
            event.setViews(event.getViews() + 1);
        }
        return event;
    }

    private User getUser(Integer userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    }

    private Category getCategory(Integer categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() -> new CategoryNotFoundException(categoryId));
    }

    private Event setDescriptionFields(Event from, Event to) {
        if (from.getAnnotation() != null) {
            to.setAnnotation(from.getAnnotation());
        }
        if (from.getDescription() != null) {
            to.setDescription(from.getDescription());
        }
        if (from.getTitle() != null) {
            to.setTitle(from.getTitle());
        }
        if (from.getEventDate() != null) {
            to.setEventDate(from.getEventDate());
        }
        if (from.getLocation() != null) {
            to.setLocation(getLocationWithId(from.getLocation()));
        }
        if (from.getPaid() != null) {
            to.setPaid(from.getPaid());
        }
        if (from.getParticipantLimit() != null) {
            to.setParticipantLimit(from.getParticipantLimit());
        }
        if (from.getRequestModeration() != null) {
            to.setRequestModeration(from.getRequestModeration());
        }

        return to;
    }

    private Location getLocationWithId(Location location) {
        return locationRepository.findByLatAndLon(location.getLat(), location.getLon()).orElseGet(() -> locationRepository.save(location));
    }

    private void saveHit(String ip, String url) {
        EndpointHitDto endpointHitDto = new EndpointHitDto(null, "main-service", url, ip, LocalDateTime.now());
        ewmStatsClient.saveEndpointHit(endpointHitDto);
    }
}
