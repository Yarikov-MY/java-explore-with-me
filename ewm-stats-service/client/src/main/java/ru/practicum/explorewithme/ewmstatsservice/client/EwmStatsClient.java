package ru.practicum.explorewithme.ewmstatsservice.client;

import ru.practicum.explorewithme.ewmstatsservice.dto.EndpointHitDto;
import ru.practicum.explorewithme.ewmstatsservice.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface EwmStatsClient {
    void saveEndpointHit(EndpointHitDto endpointHitDto);

    List<ViewStatsDto> getViewStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
