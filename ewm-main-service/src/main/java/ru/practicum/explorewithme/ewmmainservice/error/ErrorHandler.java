package ru.practicum.explorewithme.ewmmainservice.error;

import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.explorewithme.ewmmainservice.category.exception.CategoryConflictException;
import ru.practicum.explorewithme.ewmmainservice.category.exception.CategoryNotFoundException;
import ru.practicum.explorewithme.ewmmainservice.compilation.exception.CompilationNotFoundException;
import ru.practicum.explorewithme.ewmmainservice.event.exception.EventNotFoundException;
import ru.practicum.explorewithme.ewmmainservice.event.exception.InvalidEventStateException;
import ru.practicum.explorewithme.ewmmainservice.request.exception.InvalidRequestStateException;
import ru.practicum.explorewithme.ewmmainservice.request.exception.RequestNotFoundException;
import ru.practicum.explorewithme.ewmmainservice.user.exception.UserAlreadyExistsException;
import ru.practicum.explorewithme.ewmmainservice.user.exception.UserNotFoundException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler(value = {ValidationException.class, IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationAndIllegalArgExceptions(RuntimeException e) {
        return new ErrorResponse(
                HttpStatus.BAD_REQUEST,
                e.getMessage(),
                "Incorrectly made request.",
                LocalDateTime.now()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<String> errors = new ArrayList<>();
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (ObjectError error : e.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }
        return new ErrorResponse(
                HttpStatus.BAD_REQUEST,
                errors.toString(),
                "Incorrectly made request.",
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(value = {
            CategoryNotFoundException.class,
            CompilationNotFoundException.class,
            EventNotFoundException.class,
            RequestNotFoundException.class,
            UserNotFoundException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(RuntimeException e) {
        return new ErrorResponse(
                HttpStatus.NOT_FOUND,
                e.getMessage(),
                "The required object was not found.",
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(value = {
            CategoryConflictException.class,
            InvalidEventStateException.class,
            InvalidRequestStateException.class,
            UserAlreadyExistsException.class
    })
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflictException(RuntimeException e) {
        return new ErrorResponse(
                HttpStatus.NOT_FOUND,
                e.getMessage(),
                "Change conflict",
                LocalDateTime.now()
        );
    }
}
