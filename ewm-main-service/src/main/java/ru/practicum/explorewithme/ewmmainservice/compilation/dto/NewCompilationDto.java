package ru.practicum.explorewithme.ewmmainservice.compilation.dto;


import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class NewCompilationDto {
    private Boolean pinned;
    @Size(min = 1, max = 50)
    private String title;
    private List<Integer> events;
}
