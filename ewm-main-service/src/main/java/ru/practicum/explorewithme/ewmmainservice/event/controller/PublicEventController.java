package ru.practicum.explorewithme.ewmmainservice.event.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ValidationException;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.ewmmainservice.event.dto.EventFullDto;
import ru.practicum.explorewithme.ewmmainservice.event.mapper.EventMapper;
import ru.practicum.explorewithme.ewmmainservice.event.model.Event;
import ru.practicum.explorewithme.ewmmainservice.event.model.SortParam;
import ru.practicum.explorewithme.ewmmainservice.event.model.UserEventsParams;
import ru.practicum.explorewithme.ewmmainservice.event.service.EventService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/events")
@Validated
public class PublicEventController {

    private final EventService eventService;

    public PublicEventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public List<EventFullDto> getAllEvents(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) List<Integer> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(required = false, name = "sort") String sortParam,
            @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
            @Positive @RequestParam(defaultValue = "10") Integer size,
            HttpServletRequest request
    ) {
        SortParam sort = null;
        if (sortParam != null) {
            sort = SortParam.from(sortParam).orElseThrow(() -> new ValidationException("Неизвестный параметр sort " + sortParam));
        }
        if (rangeStart != null && rangeEnd != null && rangeEnd.isBefore(rangeStart)) {
            throw new ValidationException("rangeEnd раньше rangeStart");
        }
        UserEventsParams userEventsParams = UserEventsParams.builder()
                .sort(sort)
                .text(text)
                .categories(categories)
                .paid(paid)
                .startLocal(rangeStart)
                .endLocal(rangeEnd)
                .onlyAvailable(onlyAvailable)
                .from(from)
                .size(size)
                .build();
        List<Event> events = eventService.getAllEvents(userEventsParams, request.getRemoteAddr());
        return events.stream().map(EventMapper::toEventFullDto).collect(Collectors.toList());
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEventById(@PathVariable Integer eventId, HttpServletRequest request) {
        Event event = eventService.getEventById(eventId, request.getRemoteAddr());
        return EventMapper.toEventFullDto(event);
    }
}
