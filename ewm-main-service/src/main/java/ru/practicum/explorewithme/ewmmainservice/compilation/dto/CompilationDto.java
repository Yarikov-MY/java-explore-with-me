package ru.practicum.explorewithme.ewmmainservice.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.explorewithme.ewmmainservice.event.dto.EventShortDto;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class CompilationDto {
    private int id;
    private boolean pinned;
    private String title;
    private List<EventShortDto> events;
}
