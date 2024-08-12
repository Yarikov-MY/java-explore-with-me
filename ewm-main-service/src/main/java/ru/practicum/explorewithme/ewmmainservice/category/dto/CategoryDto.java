package ru.practicum.explorewithme.ewmmainservice.category.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoryDto {
    Integer id;
    @NotBlank
    String name;
}
