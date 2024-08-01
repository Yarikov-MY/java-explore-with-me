package ru.practicum.explorewithme.ewmstatsservice.server.mapper;

import org.springframework.lang.NonNull;
import ru.practicum.explorewithme.ewmstatsservice.dto.EndpointHitDto;
import ru.practicum.explorewithme.ewmstatsservice.server.model.EndpointHit;

public class EndpointHitMapper {
    public static EndpointHit toEndpointHit(@NonNull EndpointHitDto endpointHitDto) {
        return new EndpointHit(null, endpointHitDto.getApp(), endpointHitDto.getUri(), endpointHitDto.getIp(), endpointHitDto.getTimestamp());
    }
}
