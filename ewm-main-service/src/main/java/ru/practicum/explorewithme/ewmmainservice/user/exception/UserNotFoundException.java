package ru.practicum.explorewithme.ewmmainservice.user.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(int userId) {
        super("Пользователь с id=" + userId + " не найден!");
    }
}