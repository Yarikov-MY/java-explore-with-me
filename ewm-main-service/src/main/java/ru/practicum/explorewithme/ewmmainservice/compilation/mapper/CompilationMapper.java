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
        return CompilationDto.builder()
                .id(compilation.getId())
                .pinned(compilation.getPinned() != null ? compilation.getPinned() : false)
                .title(compilation.getTitle())
                .events(compilation.getEvents() != null ? compilation.getEvents().stream()
                        .map(EventMapper::toEventShortDto)
                        .collect(Collectors.toList()) : Collections.emptyList())
                .build();
    }

    public static Compilation toCompilation(NewCompilationDto editableCompilationDTO) {
        Compilation.CompilationBuilder compilationBuilder = Compilation.builder()
                .title(editableCompilationDTO.getTitle())
                .pinned(editableCompilationDTO.getPinned() != null ? editableCompilationDTO.getPinned() : false);

        if (editableCompilationDTO.getEvents() != null && !editableCompilationDTO.getEvents().isEmpty()) {
            compilationBuilder.events(editableCompilationDTO.getEvents()
                    .stream().map(eventId -> Event.builder().id(eventId).build())
                    .collect(Collectors.toSet()));
        }
        return compilationBuilder.build();
    }

}
