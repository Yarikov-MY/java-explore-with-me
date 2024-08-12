package ru.practicum.explorewithme.ewmmainservice.compilation.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Size;
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
