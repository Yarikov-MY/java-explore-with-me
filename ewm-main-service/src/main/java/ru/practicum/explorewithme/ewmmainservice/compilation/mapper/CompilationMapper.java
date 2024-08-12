package ru.practicum.explorewithme.ewmmainservice.compilation.mapper;


import lombok.NonNull;
import ru.practicum.explorewithme.ewmmainservice.compilation.dto.CompilationDto;
import ru.practicum.explorewithme.ewmmainservice.compilation.dto.NewCompilationDto;
import ru.practicum.explorewithme.ewmmainservice.compilation.model.Compilation;
import ru.practicum.explorewithme.ewmmainservice.event.mapper.EventMapper;
import ru.practicum.explorewithme.ewmmainservice.event.model.Event;

import java.util.Collections;
import java.util.stream.Collectors;

public class CompilationMapper {
    public static CompilationDto toCompilationDto(@NonNull Compilation compilation) {
        return new CompilationDto(
                compilation.getId(),
                compilation.getPinned() != null ? compilation.getPinned() : false,
                compilation.getTitle(),
                compilation.getEvents() != null ? compilation.getEvents().stream()
                        .map(EventMapper::toEventShortDto)
                        .collect(Collectors.toList()) : Collections.emptyList()
        );
    }

    public static Compilation toCompilation(NewCompilationDto editableCompilationDTO) {
        Compilation compilation = new Compilation(
                null,
                editableCompilationDTO.getTitle(),
                editableCompilationDTO.getPinned() != null ? editableCompilationDTO.getPinned() : false,
                null
        );
        if (editableCompilationDTO.getEvents() != null && !editableCompilationDTO.getEvents().isEmpty()) {
            compilation.setEvents(editableCompilationDTO.getEvents()
                    .stream().map(eventId -> Event.builder().id(eventId).build())
                    .collect(Collectors.toSet()));
        }
        return compilation;
    }

}
