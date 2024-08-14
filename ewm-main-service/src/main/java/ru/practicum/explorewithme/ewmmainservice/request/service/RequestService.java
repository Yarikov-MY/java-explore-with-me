package ru.practicum.explorewithme.ewmmainservice.request.service;


import ru.practicum.explorewithme.ewmmainservice.request.model.Request;
import ru.practicum.explorewithme.ewmmainservice.request.model.RequestStatus;

import java.util.List;

public interface RequestService {
    List<Request> getAllUserRequests(Integer userId);

    List<Request> getEventParticipants(Integer userId, Integer eventId);

    Request addParticipationRequest(Integer userId, Integer eventId);

    Request cancelRequest(Integer userId, Integer requestId);

    List<Request> changeStatusOfAllRequests(Integer userId, Integer eventId, List<Integer> requestIds, RequestStatus requestStatus);
}
