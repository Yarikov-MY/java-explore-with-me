package ru.practicum.explorewithme.ewmmainservice.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoryDto {
    Integer id;
    @NotBlank
    @Size(max = 50)
    String name;
}
