package ru.practicum.explorewithme.ewmstatsservice.server.mapper;

import org.springframework.lang.NonNull;
import ru.practicum.explorewithme.ewmstatsservice.dto.ViewStatsDto;
import ru.practicum.explorewithme.ewmstatsservice.server.model.ViewStats;

public class ViewStatsMapper {
    public static ViewStatsDto toViewStatsDto(@NonNull ViewStats viewStats) {
        return new ViewStatsDto(viewStats.getApp(), viewStats.getUri(), viewStats.getHits());
    }
}
