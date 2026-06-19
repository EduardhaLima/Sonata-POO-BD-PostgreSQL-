package com.sonata.api.service;

import com.sonata.api.dto.WorkDTO;
import com.sonata.api.exception.NotFoundException;
import com.sonata.api.model.Work;
import com.sonata.api.repository.WorkRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/** Regras de negocio para Work (obra). */
@Service
public class WorkService {

    private final WorkRepository repository;
    private final ComposerService composerService;

    public WorkService(WorkRepository repository, ComposerService composerService) {
        this.repository = repository;
        this.composerService = composerService;
    }

    public List<Work> findAll() { return repository.findAll(); }

    public Work findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Obra " + id + " nao encontrada"));
    }

    public List<Work> findByComposer(Long composerId) {
        return repository.findByComposer_ComposerId(composerId);
    }

    public Work create(WorkDTO dto) {
        Work w = new Work();
        return repository.save(apply(w, dto));
    }

    public Work update(Long id, WorkDTO dto) {
        Work w = findById(id);
        return repository.save(apply(w, dto));
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException("Obra " + id + " nao encontrada");
        }
        repository.deleteById(id);
    }

    private Work apply(Work w, WorkDTO dto) {
        w.setTitle(dto.title());
        w.setOpusNumber(dto.opusNumber()); // obrigatorio - validado no DTO
        w.setCompositionYear(dto.compositionYear());
        w.setInstrumentation(dto.instrumentation());
        w.setComposer(composerService.findById(dto.composerId()));
        return w;
    }
}
