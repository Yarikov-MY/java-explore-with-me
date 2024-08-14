package ru.practicum.explorewithme.ewmmainservice.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CategoryDto {
    Integer id;
    @NotBlank
    @Size(max = 50)
    String name;
}
