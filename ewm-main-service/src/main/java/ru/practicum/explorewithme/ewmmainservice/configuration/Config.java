package ru.practicum.explorewithme.ewmmainservice.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.explorewithme.ewmstatsservice.client.EwmStatsClient;
import ru.practicum.explorewithme.ewmstatsservice.client.EwmStatsClientImpl;

@Configuration
public class Config {
    @Bean
    public EwmStatsClient ewmStatsClient(
            @Value("${ewm-stats-service.url}") String ewmStatsServiceUrl, RestTemplateBuilder restTemplateBuilder
    ) {
        return new EwmStatsClientImpl(ewmStatsServiceUrl, restTemplateBuilder);
    }
}
