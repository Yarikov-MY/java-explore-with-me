package ru.practicum.explorewithme.ewmmainservice.compilation.service;


import ru.practicum.explorewithme.ewmmainservice.compilation.model.Compilation;

import java.util.List;

public interface CompilationService {
    List<Compilation> getCompilations(Boolean pinned, Integer from, Integer size);

    Compilation getCompilation(Integer compilationId);

    Compilation createCompilation(Compilation compilation);

    void deleteCompilation(Integer compilationId);

    Compilation updateCompilation(Integer compilationId, Compilation compilation);
}