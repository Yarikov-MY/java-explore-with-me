package ru.practicum.explorewithme.ewmmainservice.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.ewmmainservice.event.dto.EventFullDto;
import ru.practicum.explorewithme.ewmmainservice.event.dto.EventRequestStatusResultDto;
import ru.practicum.explorewithme.ewmmainservice.event.dto.EventRequestStatusUpdateRequestDto;
import ru.practicum.explorewithme.ewmmainservice.event.dto.NewEventDto;
import ru.practicum.explorewithme.ewmmainservice.event.dto.UpdateEventDto;
import ru.practicum.explorewithme.ewmmainservice.event.mapper.EventMapper;
import ru.practicum.explorewithme.ewmmainservice.event.model.Event;
import ru.practicum.explorewithme.ewmmainservice.event.model.StateAction;
import ru.practicum.explorewithme.ewmmainservice.event.service.EventService;
import ru.practicum.explorewithme.ewmmainservice.request.dto.RequestDto;
import ru.practicum.explorewithme.ewmmainservice.request.mapper.RequestMapper;
import ru.practicum.explorewithme.ewmmainservice.request.model.Request;
import ru.practicum.explorewithme.ewmmainservice.request.model.RequestStatus;
import ru.practicum.explorewithme.ewmmainservice.request.service.RequestService;

import javax.validation.Valid;
import javax.validation.ValidationException;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users/{userId}/events")
@Validated
@RequiredArgsConstructor
public class PrivateEventController {

    private final EventService eventService;
    private final RequestService requestService;

    @GetMapping
    public List<EventFullDto> getEventsByUserId(
            @PathVariable Integer userId,
            @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
            @Positive @RequestParam(defaultValue = "10") Integer size
    ) {
        return eventService.getEventsByUserId(userId, from, size).stream().map(EventMapper::toEventFullDto).collect(Collectors.toList());
    }

    @GetMapping("/{eventId}")
    public EventFullDto getUserEventById(@PathVariable Integer userId, @PathVariable Integer eventId) {
        Event event = eventService.getUserEventById(userId, eventId);
        return EventMapper.toEventFullDto(event);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addEvent(@PathVariable Integer userId, @Valid @RequestBody NewEventDto newEventDto) {
        Event event = eventService.addEvent(userId, EventMapper.toEvent(newEventDto));
        return EventMapper.toEventFullDto(event);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(
            @PathVariable Integer userId, @PathVariable Integer eventId, @Valid @RequestBody UpdateEventDto updateEventDto
    ) {
        StateAction stateAction = null;
        if (updateEventDto.getStateAction() != null) {
            stateAction = StateAction.from(updateEventDto.getStateAction()).orElseThrow(() ->
                    new ValidationException("Неизвестный StateAction " + updateEventDto.getStateAction())
            );
        }
        Event event = eventService.updateEvent(userId, eventId, stateAction, EventMapper.toEvent(updateEventDto));
        return EventMapper.toEventFullDto(event);
    }

    @GetMapping("/{eventId}/requests")
    public List<RequestDto> getEventParticipants(@PathVariable Integer userId, @PathVariable Integer eventId) {
        return requestService.getEventParticipants(userId, eventId).stream().map(RequestMapper::toRequestDto).collect(Collectors.toList());
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusResultDto updateRequests(
            @PathVariable Integer userId, @PathVariable Integer eventId,
            @Valid @RequestBody EventRequestStatusUpdateRequestDto req
    ) {
        if (req.getStatus() != RequestStatus.CONFIRMED && req.getStatus() != RequestStatus.REJECTED)
            throw new ValidationException("Неизвестный RequestStatus для изменения - " + req.getStatus().name());

        List<Request> requests = requestService.changeStatusOfAllRequests(userId, eventId, req.getRequestIds(), req.getStatus());
        List<RequestDto> confirmedRequests = new ArrayList<>();
        List<RequestDto> rejectedRequests = new ArrayList<>();
        requests.forEach(request -> {
            if (request.getStatus() == RequestStatus.CONFIRMED) {
                confirmedRequests.add(RequestMapper.toRequestDto(request));
            }
            if (request.getStatus() == RequestStatus.REJECTED) {
                rejectedRequests.add(RequestMapper.toRequestDto(request));
            }
        });
        return new EventRequestStatusResultDto(confirmedRequests, rejectedRequests);
    }

}
