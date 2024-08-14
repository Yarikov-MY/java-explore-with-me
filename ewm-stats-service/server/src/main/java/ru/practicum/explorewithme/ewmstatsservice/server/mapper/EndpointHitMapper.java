package ru.practicum.explorewithme.ewmstatsservice.server.mapper;

import org.springframework.lang.NonNull;
import ru.practicum.explorewithme.ewmstatsservice.dto.EndpointHitDto;
import ru.practicum.explorewithme.ewmstatsservice.server.model.EndpointHit;

public class EndpointHitMapper {
    public static EndpointHit toEndpointHit(@NonNull EndpointHitDto endpointHitDto) {
        return EndpointHit.builder()
                .app(endpointHitDto.getApp())
                .uri(endpointHitDto.getUri())
                .ip(endpointHitDto.getIp())
                .timestamp(endpointHitDto.getTimestamp())
                .build();
    }
}
