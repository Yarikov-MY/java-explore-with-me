package ru.practicum.explorewithme.ewmmainservice.category.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class CategoryDto {
    Integer id;
    @NotBlank
    String name;
}
