package ru.practicum.explorewithme.ewmmainservice.category.mapper;

import org.springframework.lang.NonNull;
import ru.practicum.explorewithme.ewmmainservice.category.dto.CategoryDto;
import ru.practicum.explorewithme.ewmmainservice.category.model.Category;


public class CategoryMapper {
    public static CategoryDto toCategoryDto(@NonNull Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();

    }

    public static Category toCategory(@NonNull CategoryDto categoryDto) {
        return Category.builder()
                .name(categoryDto.getName())
                .build();
    }
}
