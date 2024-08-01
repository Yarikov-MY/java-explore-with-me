package ru.practicum.explorewithme.ewmstatsservice.server.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.ewmstatsservice.server.model.EndpointHit;
import ru.practicum.explorewithme.ewmstatsservice.server.model.ViewStats;
import ru.practicum.explorewithme.ewmstatsservice.server.repository.ViewStatsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ViewStatsServiceImpl implements ViewStatsService {
    private final ViewStatsRepository viewStatsRepository;

    public ViewStatsServiceImpl(ViewStatsRepository viewStatsRepository) {
        this.viewStatsRepository = viewStatsRepository;
    }

    @Override
    @Transactional
    public void saveEndpointHit(EndpointHit endpointHit) {
        viewStatsRepository.save(endpointHit);
    }

    @Override
    public List<ViewStats> getViewStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        if (uris == null || uris.isEmpty()) {
            if (unique) {
                return viewStatsRepository.findUniqueAll(start, end);
            } else {
                return viewStatsRepository.findAll(start, end);
            }
        } else {
            if (unique) {
                return viewStatsRepository.findUniqueAllByUris(start, end, uris);
            } else {
                return viewStatsRepository.findAllByUris(start, end, uris);
            }
        }
    }
}
