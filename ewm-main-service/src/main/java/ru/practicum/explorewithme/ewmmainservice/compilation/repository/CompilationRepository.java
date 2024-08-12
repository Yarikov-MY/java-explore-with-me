package ru.practicum.explorewithme.ewmmainservice.compilation.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.ewmmainservice.compilation.model.Compilation;

public interface CompilationRepository extends JpaRepository<Compilation, Integer> {
    Page<Compilation> findByPinned(Boolean pinned, Pageable pageable);
}
