package ru.practicum.explorewithme.ewmmainservice.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.ewmmainservice.event.model.Location;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Integer> {
    Optional<Location> findByLatAndLon(float lat, float lon);
}
