package ru.practicum.explorewithme.ewmmainservice.event.controller;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.ewmmainservice.event.dto.EventFullDto;
import ru.practicum.explorewithme.ewmmainservice.event.dto.UpdateEventDto;
import ru.practicum.explorewithme.ewmmainservice.event.mapper.EventMapper;
import ru.practicum.explorewithme.ewmmainservice.event.model.AdminEventsParams;
import ru.practicum.explorewithme.ewmmainservice.event.model.Event;
import ru.practicum.explorewithme.ewmmainservice.event.model.EventState;
import ru.practicum.explorewithme.ewmmainservice.event.model.StateAction;
import ru.practicum.explorewithme.ewmmainservice.event.service.EventService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/events")
@Validated
public class AdminEventController {
    private final EventService eventService;

    public AdminEventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public List<EventFullDto> getAllEvents(
            @RequestParam(required = false) List<Integer> users,
            @RequestParam(required = false) List<String> states,
            @RequestParam(required = false) List<Integer> categories,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        List<EventState> eventStates = null;
        if (states != null) {
            eventStates = new ArrayList<>();
            for (String state : states) {
                EventState eventState = EventMapper.parseEventState(state).orElseThrow(() -> new ValidationException("Неизвестный EventState - " + state));
                eventStates.add(eventState);
            }
        }
        if (rangeStart != null && rangeEnd != null && rangeEnd.isBefore(rangeStart)) {
            throw new ValidationException("rangeEnd раньше rangeStart");
        }
        AdminEventsParams adminEventsParams = AdminEventsParams.builder()
                .users(users)
                .states(eventStates)
                .categories(categories)
                .startLocal(rangeStart)
                .endLocal(rangeEnd)
                .from(from)
                .size(size)
                .build();
        List<Event> events = eventService.getAllEvents(adminEventsParams);
        return events.stream().map(EventMapper::toEventFullDto).collect(Collectors.toList());
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@PathVariable Integer eventId, @RequestBody @Valid UpdateEventDto updateEventDto) {
        StateAction stateAction = null;
        if (updateEventDto.getStateAction() != null) {
            stateAction = StateAction.from(updateEventDto.getStateAction()).orElseThrow(() ->
                    new ValidationException("Неизвестный EventState " + updateEventDto.getStateAction())
            );
        }
        Event event = eventService.updateEvent(eventId, stateAction, EventMapper.toEvent(updateEventDto));
        return EventMapper.toEventFullDto(event);
    }
}
