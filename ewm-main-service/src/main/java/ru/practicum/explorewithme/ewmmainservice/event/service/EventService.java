package ru.practicum.explorewithme.ewmmainservice.event.service;


import ru.practicum.explorewithme.ewmmainservice.event.model.AdminEventsParams;
import ru.practicum.explorewithme.ewmmainservice.event.model.Event;
import ru.practicum.explorewithme.ewmmainservice.event.model.StateAction;
import ru.practicum.explorewithme.ewmmainservice.event.model.UserEventsParams;

import java.util.List;

public interface EventService {
    List<Event> getEventsByUserId(Integer userId, Integer from, Integer size);

    Event getUserEventById(Integer userId, Integer eventId);

    Event addEvent(Integer userId, Event event);

    Event updateEvent(Integer userId, Integer eventId, StateAction stateAction, Event event);

    List<Event> getAllEvents(AdminEventsParams adminEventsParams);

    Event updateEvent(Integer eventId, StateAction stateAction, Event event);

    List<Event> getAllEvents(UserEventsParams userEventsParams, String ip);

    Event getEventById(Integer eventId, String ip);
}
