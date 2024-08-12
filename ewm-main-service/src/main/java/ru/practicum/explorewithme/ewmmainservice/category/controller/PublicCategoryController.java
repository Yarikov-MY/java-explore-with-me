package ru.practicum.explorewithme.ewmmainservice.category.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.ewmmainservice.category.dto.CategoryDto;
import ru.practicum.explorewithme.ewmmainservice.category.mapper.CategoryMapper;
import ru.practicum.explorewithme.ewmmainservice.category.service.CategoryService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/categories")
@Validated
public class PublicCategoryController {
    private final CategoryService categoryService;

    public PublicCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<CategoryDto> getAllCategories(
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        return categoryService.getAllCategories(from, size).stream().map(CategoryMapper::toCategoryDto).collect(Collectors.toList());
    }

    @GetMapping("/{catId}")
    public CategoryDto getCategory(@PathVariable Integer catId) {
        return CategoryMapper.toCategoryDto(categoryService.getCategory(catId));
    }
}
