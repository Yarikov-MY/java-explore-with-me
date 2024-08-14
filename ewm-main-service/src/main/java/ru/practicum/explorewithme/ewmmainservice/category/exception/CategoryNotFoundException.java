package ru.practicum.explorewithme.ewmmainservice.category.exception;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(Integer id) {
        super("Категория с id=" + id + " не найдена!");
    }
}
