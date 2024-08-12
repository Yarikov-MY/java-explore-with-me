package ru.practicum.explorewithme.ewmmainservice.category.service;


import ru.practicum.explorewithme.ewmmainservice.category.model.Category;

import java.util.List;

public interface CategoryService {

    List<Category> getAllCategories(Integer from, Integer size);

    Category getCategory(Integer categoryId);

    Category addCategory(Category category);

    Category updateCategory(Integer categoryId, Category category);

    void deleteCategory(Integer categoryId);


}
