package ru.practicum.explorewithme.ewmmainservice.compilation.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.ewmmainservice.compilation.exception.CompilationNotFoundException;
import ru.practicum.explorewithme.ewmmainservice.compilation.model.Compilation;
import ru.practicum.explorewithme.ewmmainservice.compilation.repository.CompilationRepository;
import ru.practicum.explorewithme.ewmmainservice.event.model.Event;
import ru.practicum.explorewithme.ewmmainservice.event.repository.EventRepository;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    public CompilationServiceImpl(CompilationRepository compilationRepository, EventRepository eventRepository) {
        this.compilationRepository = compilationRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    @Transactional
    public List<Compilation> getCompilations(Boolean pinned, Integer from, Integer size) {
        return compilationRepository.findByPinned(pinned, PageRequest.of(from / size, size)).getContent();
    }

    @Override
    @Transactional
    public Compilation getCompilation(Integer compilationId) {
        return findCompilation(compilationId);
    }

    @Override
    @Transactional
    public Compilation createCompilation(Compilation compilation) {
        if (compilation.getEvents() != null && !compilation.getEvents().isEmpty()) {
            compilation.setEvents(
                    new HashSet<>(eventRepository.findAllByIdIn(compilation.getEvents().stream()
                            .map(Event::getId)
                            .collect(Collectors.toList())))
            );
        }
        return compilationRepository.save(compilation);
    }

    @Override
    @Transactional
    public void deleteCompilation(Integer compilationId) {
        Compilation compilation = findCompilation(compilationId);
        compilationRepository.delete(compilation);
    }

    @Override
    @Transactional
    public Compilation updateCompilation(Integer compilationId, Compilation compilation) {
        Compilation targetCompilation = findCompilation(compilationId);
        if (compilation.getTitle() != null) {
            targetCompilation.setTitle(compilation.getTitle());
        }
        if (compilation.getPinned() != null) {
            targetCompilation.setPinned(compilation.getPinned());
        }
        if (compilation.getEvents() != null && !compilation.getEvents().isEmpty()) {
            List<Event> events = eventRepository.findAllByIdIn(compilation.getEvents().stream().map(Event::getId).collect(Collectors.toList()));
            targetCompilation.setEvents(new HashSet<>(events));
        }
        return compilationRepository.save(targetCompilation);
    }

    private Compilation findCompilation(Integer compilationId) {
        return compilationRepository.findById(compilationId).orElseThrow(() -> new CompilationNotFoundException(compilationId));
    }
}
