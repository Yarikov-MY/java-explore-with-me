package ru.practicum.explorewithme.ewmmainservice.category.mapper;

import org.springframework.lang.NonNull;
import ru.practicum.explorewithme.ewmmainservice.category.dto.CategoryDto;
import ru.practicum.explorewithme.ewmmainservice.category.model.Category;


public class CategoryMapper {
    public static CategoryDto toCategoryDto(@NonNull Category category) {
        return new CategoryDto(category.getId(), category.getName());
    }

    public static Category toCategory(@NonNull CategoryDto categoryDto) {
        return new Category(null, categoryDto.getName());
    }
}
