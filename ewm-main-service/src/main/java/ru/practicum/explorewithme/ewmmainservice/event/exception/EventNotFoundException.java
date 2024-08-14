package ru.practicum.explorewithme.ewmmainservice.event.exception;


public class EventNotFoundException extends RuntimeException {
    public EventNotFoundException(int eventId) {
        super("Событие с id=" + eventId + " не найдено");
    }

    public EventNotFoundException(int eventId, int userId) {
        super("Событие с id=" + eventId + " пользователя id=" + userId + " не найдено");
    }

    public EventNotFoundException(String message) {
        super(message);
    }
}
