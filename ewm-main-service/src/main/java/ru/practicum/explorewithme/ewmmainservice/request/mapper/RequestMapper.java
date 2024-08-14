package ru.practicum.explorewithme.ewmmainservice.request.mapper;

import jakarta.validation.constraints.NotNull;
import lombok.experimental.UtilityClass;
import ru.practicum.explorewithme.ewmmainservice.request.dto.RequestDto;
import ru.practicum.explorewithme.ewmmainservice.request.model.Request;

@UtilityClass
public class RequestMapper {
    public static RequestDto toRequestDto(@NotNull Request request) {
        return RequestDto.builder()
                .id(request.getId())
                .created(request.getCreated())
                .event(request.getEvent().getId())
                .requester(request.getRequester().getId())
                .status(request.getStatus())
                .build();
    }
}
