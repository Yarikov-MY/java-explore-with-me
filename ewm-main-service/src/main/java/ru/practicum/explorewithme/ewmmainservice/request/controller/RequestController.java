package ru.practicum.explorewithme.ewmmainservice.request.controller;

import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.ewmmainservice.request.dto.RequestDto;
import ru.practicum.explorewithme.ewmmainservice.request.mapper.RequestMapper;
import ru.practicum.explorewithme.ewmmainservice.request.service.RequestService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users/{userId}/requests")
@Validated
public class RequestController {
    private final RequestService requestService;

    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @GetMapping
    public List<RequestDto> getAllUserRequests(@PathVariable Integer userId) {
        return requestService.getAllUserRequests(userId).stream().map(RequestMapper::toRequestDto).collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RequestDto add(
            @PathVariable Integer userId,
            @NotNull(message = "В запросе отсутствует id события")
            @RequestParam Integer eventId
    ) {
        return RequestMapper.toRequestDto(requestService.addParticipationRequest(userId, eventId));
    }

    @PatchMapping("/{requestId}/cancel")
    public RequestDto cancelRequest(@PathVariable Integer userId, @PathVariable Integer requestId) {
        return RequestMapper.toRequestDto(requestService.cancelRequest(userId, requestId));
    }
}
