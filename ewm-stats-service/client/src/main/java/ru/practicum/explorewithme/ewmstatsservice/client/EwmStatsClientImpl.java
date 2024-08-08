package ru.practicum.explorewithme.ewmstatsservice.client;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.lang.Nullable;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.explorewithme.ewmstatsservice.dto.EndpointHitDto;
import ru.practicum.explorewithme.ewmstatsservice.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class EwmStatsClientImpl implements EwmStatsClient {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final RestTemplate restTemplate;

    public EwmStatsClientImpl(String url, RestTemplateBuilder restTemplateBuilder) {
        restTemplate = restTemplateBuilder
                .uriTemplateHandler(new DefaultUriBuilderFactory(url))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
    }

    @Override
    public void saveEndpointHit(EndpointHitDto endpointHitDto) {
        try {
            restTemplate.postForEntity("/hit", new HttpEntity<>(endpointHitDto), Void.class);
        } catch (RestClientException ex) {
            throw new EwmStatsClientException(ex.getMessage());
        }
    }

    @Override
    public List<ViewStatsDto> getViewStats(LocalDateTime start, LocalDateTime end, @Nullable List<String> uris, Boolean unique) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString("/stats")
                .queryParam("start", start.format(DATE_TIME_FORMATTER))
                .queryParam("end", end.format(DATE_TIME_FORMATTER))
                .queryParam("unique", unique);
        if (uris != null && !uris.isEmpty()) {
            uriBuilder.queryParam("uris", String.join(",", uris));
        }
        try {
            ViewStatsDto[] responseArray = restTemplate.getForObject(uriBuilder.build().toString(), ViewStatsDto[].class);
            if (responseArray != null) {
                return Arrays.asList(responseArray);
            } else {
                return Collections.emptyList();
            }
        } catch (EwmStatsClientException ex) {
            throw new EwmStatsClientException(ex.getMessage());
        }
    }
}
