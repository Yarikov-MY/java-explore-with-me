package ru.practicum.explorewithme.ewmmainservice.category.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.ewmmainservice.category.exception.CategoryConflictException;
import ru.practicum.explorewithme.ewmmainservice.category.exception.CategoryNotFoundException;
import ru.practicum.explorewithme.ewmmainservice.category.model.Category;
import ru.practicum.explorewithme.ewmmainservice.category.repository.CategoryRepository;
import ru.practicum.explorewithme.ewmmainservice.event.repository.EventRepository;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository, EventRepository eventRepository) {
        this.categoryRepository = categoryRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    @Transactional
    public List<Category> getAllCategories(Integer from, Integer size) {
        return categoryRepository.findAll(PageRequest.of(from / size, size)).getContent();
    }

    @Override
    @Transactional
    public Category getCategory(Integer categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() -> new CategoryNotFoundException(categoryId));
    }

    @Override
    @Transactional
    public Category addCategory(Category category) {
        if (categoryRepository.findByName(category.getName()).isPresent()) {
            throw new CategoryConflictException("Категория с таким именем уже существует!");
        }
        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public Category updateCategory(Integer categoryId, Category category) {
        Category targetCategory = getCategory(categoryId);
        if (targetCategory.getName().equals(category.getName())) {
            return targetCategory;
        }
        if (categoryRepository.findByName(category.getName()).isPresent()) {
            throw new CategoryConflictException("Категория с таким именем уже существует!");
        }
        targetCategory.setName(category.getName());
        return categoryRepository.save(targetCategory);
    }

    @Override
    @Transactional
    public void deleteCategory(Integer categoryId) {
        if (eventRepository.existsByCategoryId(categoryId)) {
            throw new CategoryConflictException("Добавлены события с данной категорией!");
        }
        if (categoryRepository.findById(categoryId).isEmpty()) {
            throw new CategoryNotFoundException(categoryId);
        }
        categoryRepository.deleteById(categoryId);
    }
}

