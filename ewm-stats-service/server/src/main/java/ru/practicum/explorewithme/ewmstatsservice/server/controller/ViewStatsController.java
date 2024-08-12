package ru.practicum.explorewithme.ewmstatsservice.server.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.explorewithme.ewmstatsservice.dto.EndpointHitDto;
import ru.practicum.explorewithme.ewmstatsservice.dto.ViewStatsDto;
import ru.practicum.explorewithme.ewmstatsservice.server.mapper.EndpointHitMapper;
import ru.practicum.explorewithme.ewmstatsservice.server.mapper.ViewStatsMapper;
import ru.practicum.explorewithme.ewmstatsservice.server.model.ViewStats;
import ru.practicum.explorewithme.ewmstatsservice.server.service.ViewStatsService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Validated
public class ViewStatsController {
    private final ViewStatsService viewStatsService;

    public ViewStatsController(ViewStatsService viewStatsService) {
        this.viewStatsService = viewStatsService;
    }

    @PostMapping("/hit")
    public ResponseEntity<Void> saveEndpointHit(@Valid @RequestBody EndpointHitDto endpointHitDto) {
        viewStatsService.saveEndpointHit(EndpointHitMapper.toEndpointHit(endpointHitDto));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/stats")
    public List<ViewStatsDto> getStats(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
            @RequestParam(required = false) List<String> uris,
            @RequestParam(defaultValue = "false") Boolean unique
    ) {
        if (start.isAfter(end)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "end is before start!");
        }
        List<ViewStats> viewStats = viewStatsService.getViewStats(start, end, uris, unique);
        return viewStats.stream().map(ViewStatsMapper::toViewStatsDto).collect(Collectors.toList());
    }
}
