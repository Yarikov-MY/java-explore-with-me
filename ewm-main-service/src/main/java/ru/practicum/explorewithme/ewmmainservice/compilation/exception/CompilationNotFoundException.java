package ru.practicum.explorewithme.ewmmainservice.compilation.exception;

public class CompilationNotFoundException extends RuntimeException {
    public CompilationNotFoundException(int compilationId) {
        super("Подборка с id=" + compilationId + " не найдена!");
    }
}
