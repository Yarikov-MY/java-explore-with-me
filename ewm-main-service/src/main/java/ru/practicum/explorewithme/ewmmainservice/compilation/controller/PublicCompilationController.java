package ru.practicum.explorewithme.ewmmainservice.compilation.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.ewmmainservice.compilation.dto.CompilationDto;
import ru.practicum.explorewithme.ewmmainservice.compilation.mapper.CompilationMapper;
import ru.practicum.explorewithme.ewmmainservice.compilation.service.CompilationService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/compilations")
@Validated
public class PublicCompilationController {
    private final CompilationService compilationService;

    public PublicCompilationController(CompilationService compilationService) {
        this.compilationService = compilationService;
    }

    @GetMapping
    public List<CompilationDto> getCompilations(
            @RequestParam(required = false, defaultValue = "false") Boolean pinned,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        return compilationService.getCompilations(pinned, from, size).stream().map(CompilationMapper::toCompilationDto).collect(Collectors.toList());
    }

    @GetMapping("/{compilationId}")
    public CompilationDto getCompilation(@PathVariable Integer compilationId) {
        return CompilationMapper.toCompilationDto(compilationService.getCompilation(compilationId));
    }
}
