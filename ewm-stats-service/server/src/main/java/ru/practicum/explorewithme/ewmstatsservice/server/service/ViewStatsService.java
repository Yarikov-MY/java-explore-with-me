package ru.practicum.explorewithme.ewmstatsservice.server.service;

import ru.practicum.explorewithme.ewmstatsservice.server.model.EndpointHit;
import ru.practicum.explorewithme.ewmstatsservice.server.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface ViewStatsService {
    void saveEndpointHit(EndpointHit endpointHit);

    List<ViewStats> getViewStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
