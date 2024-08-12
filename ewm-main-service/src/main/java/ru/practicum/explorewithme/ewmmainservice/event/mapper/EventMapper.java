package ru.practicum.explorewithme.ewmmainservice.event.mapper;


import lombok.NonNull;
import ru.practicum.explorewithme.ewmmainservice.category.mapper.CategoryMapper;
import ru.practicum.explorewithme.ewmmainservice.category.model.Category;
import ru.practicum.explorewithme.ewmmainservice.event.dto.EventFullDto;
import ru.practicum.explorewithme.ewmmainservice.event.dto.EventShortDto;
import ru.practicum.explorewithme.ewmmainservice.event.dto.NewEventDto;
import ru.practicum.explorewithme.ewmmainservice.event.dto.UpdateEventDto;
import ru.practicum.explorewithme.ewmmainservice.event.model.Event;
import ru.practicum.explorewithme.ewmmainservice.event.model.EventState;
import ru.practicum.explorewithme.ewmmainservice.user.mapper.UserMapper;

import java.util.Optional;

public class EventMapper {
    public static EventFullDto toEventFullDto(@NonNull Event event) {
        return EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .location(LocationMapper.toLocationDto(event.getLocation()))
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .requestModeration(event.getRequestModeration())
                .title(event.getTitle())
                .initiator(UserMapper.toUserDTO(event.getInitiator()))
                .createdOn(event.getCreatedOn())
                .publishedOn(event.getPublishedOn())
                .state(event.getState())
                .views(event.getViews())
                .build();
    }

    public static EventShortDto toEventShortDto(@NonNull Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getEventDate())
                .initiator(UserMapper.toUserDTO(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    public static Event toEvent(@NonNull NewEventDto newEventDto) {
        return Event.builder()
                .annotation(newEventDto.getAnnotation())
                .description(newEventDto.getDescription())
                .eventDate(newEventDto.getEventDate())
                .paid(newEventDto.getPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .requestModeration(newEventDto.getRequestModeration())
                .title(newEventDto.getTitle())
                .category(new Category(newEventDto.getCategory(), null))
                .location(LocationMapper.toLocation(newEventDto.getLocation()))
                .build();
    }

    public static Event toEvent(@NonNull UpdateEventDto updateEventDto) {
        return Event.builder()
                .annotation(updateEventDto.getAnnotation())
                .description(updateEventDto.getDescription())
                .eventDate(updateEventDto.getEventDate())
                .paid(updateEventDto.getPaid())
                .participantLimit(updateEventDto.getParticipantLimit())
                .requestModeration(updateEventDto.getRequestModeration())
                .title(updateEventDto.getTitle())
                .category(new Category(updateEventDto.getCategory(), null))
                .location(LocationMapper.toLocation(updateEventDto.getLocation()))
                .build();
    }

    public static Optional<EventState> parseEventState(String eventState) {
        for (EventState state : EventState.values()) {
            if (state.name().equalsIgnoreCase(eventState)) {
                return Optional.of(state);
            }
        }
        return Optional.empty();
    }
}
