package ru.practicum.explorewithme.ewmmainservice.request.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.ewmmainservice.event.exception.EventNotFoundException;
import ru.practicum.explorewithme.ewmmainservice.event.model.Event;
import ru.practicum.explorewithme.ewmmainservice.event.model.EventState;
import ru.practicum.explorewithme.ewmmainservice.event.repository.EventRepository;
import ru.practicum.explorewithme.ewmmainservice.request.exception.InvalidRequestStateException;
import ru.practicum.explorewithme.ewmmainservice.request.exception.RequestNotFoundException;
import ru.practicum.explorewithme.ewmmainservice.request.model.Request;
import ru.practicum.explorewithme.ewmmainservice.request.model.RequestStatus;
import ru.practicum.explorewithme.ewmmainservice.request.repository.RequestRepository;
import ru.practicum.explorewithme.ewmmainservice.user.exception.UserNotFoundException;
import ru.practicum.explorewithme.ewmmainservice.user.model.User;
import ru.practicum.explorewithme.ewmmainservice.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    public RequestServiceImpl(RequestRepository requestRepository, UserRepository userRepository, EventRepository eventRepository) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    @Transactional
    public List<Request> getAllUserRequests(Integer userId) {
        return requestRepository.findAllByRequesterId(userId);
    }

    @Override
    @Transactional
    public List<Request> getEventParticipants(Integer userId, Integer eventId) {
        Optional<Event> event = eventRepository.findEventByIdAndInitiatorId(eventId, userId);
        if (event.isPresent()) {
            return requestRepository.findAllByEventId(eventId);
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    @Transactional
    public Request addParticipationRequest(Integer userId, Integer eventId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        if (requestRepository.findByEventIdAndRequesterId(eventId, userId).isPresent()) {
            throw new InvalidRequestStateException("Запрос уже существует");
        }
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
        if (event.getState() != EventState.PUBLISHED) {
            throw new InvalidRequestStateException("Нельзя участвовать в неопубликованном событии!");
        }
        if (event.getEventDate().isBefore(LocalDateTime.now())) {
            throw new InvalidRequestStateException("Событие уже началось!");
        }
        if (Objects.equals(event.getInitiator().getId(), userId)) {
            throw new InvalidRequestStateException("Инициатор события не может создать запрос на участие!");
        }
        if (event.getParticipantLimit() != 0 && event.getConfirmedRequests() >= event.getParticipantLimit()) {
            throw new InvalidRequestStateException("Превышен лимит участия!");
        }
        Request request = new Request();
        request.setCreated(LocalDateTime.now());
        request.setEvent(event);
        request.setRequester(user);
        if (event.getParticipantLimit() != 0 && event.getRequestModeration()) {
            request.setStatus(RequestStatus.PENDING);
        } else {
            request.setStatus(RequestStatus.CONFIRMED);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        }
        return requestRepository.save(request);
    }

    @Override
    @Transactional
    public Request cancelRequest(Integer userId, Integer requestId) {
        Request request = requestRepository.findByIdAndRequesterId(requestId, userId).orElseThrow(() -> new RequestNotFoundException(requestId));
        request.setStatus(RequestStatus.CANCELED);
        return requestRepository.save(request);
    }

    @Override
    @Transactional
    public List<Request> changeStatusOfAllRequests(
            Integer userId, Integer eventId, List<Integer> requestIds, RequestStatus requestStatus
    ) {
        Event event = eventRepository.findEventByIdAndInitiatorId(eventId, userId).orElseThrow(() -> new EventNotFoundException(eventId, userId));
        if (event.getParticipantLimit() == 0 || !event.getRequestModeration()) {
            return Collections.emptyList();
        }
        if (event.getConfirmedRequests() >= event.getParticipantLimit()) {
            throw new InvalidRequestStateException("Превышен лимит участия!");
        }
        List<Request> requests = requestRepository.findAllById(requestIds);
        if (requests.stream().anyMatch(request -> request.getStatus() != RequestStatus.PENDING)) {
            throw new InvalidRequestStateException("У заявок, находящихся только в состоянии ожидания, можно изменить статус!");
        }
        if (requestStatus == RequestStatus.CONFIRMED) {
            requests.forEach(request -> {
                if (event.getConfirmedRequests() <= event.getParticipantLimit()) {
                    request.setStatus(RequestStatus.CONFIRMED);
                    event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                } else {
                    request.setStatus(RequestStatus.REJECTED);
                }
            });
        } else {
            requests.forEach(request ->
                    request.setStatus(RequestStatus.REJECTED)
            );
        }
        return requests;
    }
}
