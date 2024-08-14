package ru.practicum.explorewithme.ewmmainservice.request.exception;

public class RequestNotFoundException extends RuntimeException {
    public RequestNotFoundException(int requestId) {
        super("Запрос с id=" + requestId + " не найден!");
    }
}
