package ru.practicum.explorewithme.ewmmainservice.compilation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.ewmmainservice.compilation.dto.CompilationDto;
import ru.practicum.explorewithme.ewmmainservice.compilation.dto.NewCompilationDto;
import ru.practicum.explorewithme.ewmmainservice.compilation.mapper.CompilationMapper;
import ru.practicum.explorewithme.ewmmainservice.compilation.model.Compilation;
import ru.practicum.explorewithme.ewmmainservice.compilation.service.CompilationService;

import javax.validation.Valid;
import javax.validation.ValidationException;

@RestController
@RequestMapping("/admin/compilations")
@Validated
@RequiredArgsConstructor
public class AdminCompilationController {

    private final CompilationService compilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto add(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        if (newCompilationDto.getTitle() == null || newCompilationDto.getTitle().isBlank()) {
            throw new ValidationException("Название подборки не может быть пустым");
        }
        Compilation compilation = compilationService.createCompilation(CompilationMapper.toCompilation(newCompilationDto));
        return CompilationMapper.toCompilationDto(compilation);
    }

    @PatchMapping("/{compilationId}")
    public CompilationDto patch(
            @PathVariable Integer compilationId, @Valid @RequestBody NewCompilationDto newCompilationDto
    ) {
        Compilation compilation = compilationService.updateCompilation(compilationId, CompilationMapper.toCompilation(newCompilationDto));
        return CompilationMapper.toCompilationDto(compilation);
    }

    @DeleteMapping("/{compilationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer compilationId) {
        compilationService.deleteCompilation(compilationId);
    }
}
