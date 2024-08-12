package ru.practicum.explorewithme.ewmmainservice.event.mapper;


import lombok.NonNull;
import ru.practicum.explorewithme.ewmmainservice.event.dto.LocationDto;
import ru.practicum.explorewithme.ewmmainservice.event.model.Location;

public class LocationMapper {
    public static Location toLocation(@NonNull LocationDto locationDto) {
        return new Location(null, locationDto.getLat(), locationDto.getLon());
    }

    public static LocationDto toLocationDto(@NonNull Location location) {
        return new LocationDto(location.getLat(), location.getLon());
    }
}
