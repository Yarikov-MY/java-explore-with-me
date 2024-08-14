package ru.practicum.explorewithme.ewmmainservice.event.mapper;


import lombok.NonNull;
import ru.practicum.explorewithme.ewmmainservice.event.dto.LocationDto;
import ru.practicum.explorewithme.ewmmainservice.event.model.Location;

public class LocationMapper {
    public static Location toLocation(@NonNull LocationDto locationDto) {
        return Location.builder()
                .lat(locationDto.getLat())
                .lon(locationDto.getLon())
                .build();
    }

    public static LocationDto toLocationDto(@NonNull Location location) {
        return LocationDto.builder()
                .lat(location.getLat())
                .lon(location.getLon())
                .build();
    }
}
