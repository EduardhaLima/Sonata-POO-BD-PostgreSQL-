package com.sonata.api.service;

import com.sonata.api.dto.ComposerDTO;
import com.sonata.api.exception.NotFoundException;
import com.sonata.api.model.Composer;
import com.sonata.api.repository.ComposerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/** Regras de negocio para Composer. */
@Service
public class ComposerService {

    private final ComposerRepository repository;

    public ComposerService(ComposerRepository repository) {
        this.repository = repository;
    }

    public List<Composer> findAll() { return repository.findAll(); }

    public Composer findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Compositor " + id + " nao encontrado"));
    }

    public List<Composer> findByPeriod(String period) {
        return repository.findByHistoricalPeriodIgnoreCase(period);
    }

    public Composer create(ComposerDTO dto) {
        return repository.save(toEntity(new Composer(), dto));
    }

    public Composer update(Long id, ComposerDTO dto) {
        Composer c = findById(id);
        return repository.save(toEntity(c, dto));
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException("Compositor " + id + " nao encontrado");
        }
        repository.deleteById(id);
    }

    private Composer toEntity(Composer c, ComposerDTO dto) {
        c.setName(dto.name());
        c.setHistoricalPeriod(dto.historicalPeriod());
        c.setNationality(dto.nationality());
        c.setBiography(dto.biography());
        return c;
    }
}
