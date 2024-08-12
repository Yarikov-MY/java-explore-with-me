package ru.practicum.explorewithme.ewmmainservice.request.mapper;

import jakarta.validation.constraints.NotNull;
import lombok.experimental.UtilityClass;
import ru.practicum.explorewithme.ewmmainservice.request.dto.RequestDto;
import ru.practicum.explorewithme.ewmmainservice.request.model.Request;

@UtilityClass
public class RequestMapper {
    public static RequestDto toRequestDto(@NotNull Request request) {
        return new RequestDto(request.getId(), request.getCreated(), request.getEvent().getId(), request.getRequester().getId(), request.getStatus());
    }
}
