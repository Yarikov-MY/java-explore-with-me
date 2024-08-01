package ru.practicum.explorewithme.ewmstatsservice.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.explorewithme.ewmstatsservice.server.model.EndpointHit;
import ru.practicum.explorewithme.ewmstatsservice.server.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ViewStatsRepository extends JpaRepository<EndpointHit, Integer> {
    @Query("SELECT new ru.practicum.explorewithme.ewmstatsservice.server.model.ViewStats(" +
            "e.app, e.uri, COUNT(e.ip)) " +
            "FROM EndpointHit AS e " +
            "WHERE e.timestamp BETWEEN ?1 AND ?2 " +
            "GROUP BY e.app, e.uri " +
            "ORDER BY COUNT(e.ip) DESC")
    List<ViewStats> findAll(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.explorewithme.ewmstatsservice.server.model.ViewStats(" +
            "e.app, e.uri, COUNT(e.ip)) " +
            "FROM EndpointHit AS e " +
            "WHERE e.timestamp BETWEEN ?1 AND ?2 " +
            "AND e.uri IN(?3) " +
            "GROUP BY e.app, e.uri " +
            "ORDER BY COUNT(e.ip) DESC")
    List<ViewStats> findAllByUris(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT new ru.practicum.explorewithme.ewmstatsservice.server.model.ViewStats(" +
            "e.app, e.uri, COUNT(DISTINCT e.ip)) " +
            "FROM EndpointHit AS e " +
            "WHERE e.timestamp BETWEEN ?1 AND ?2 " +
            "GROUP BY e.app, e.uri " +
            "ORDER BY COUNT(DISTINCT e.ip) DESC")
    List<ViewStats> findUniqueAll(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.explorewithme.ewmstatsservice.server.model.ViewStats(" +
            "e.app, e.uri, COUNT(DISTINCT e.ip)) " +
            "FROM EndpointHit AS e " +
            "WHERE e.timestamp BETWEEN ?1 AND ?2 " +
            "AND e.uri IN(?3) " +
            "GROUP BY e.app, e.uri " +
            "ORDER BY COUNT(DISTINCT e.ip) DESC")
    List<ViewStats> findUniqueAllByUris(LocalDateTime start, LocalDateTime end, List<String> uris);
}
