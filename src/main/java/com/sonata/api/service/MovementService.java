package com.sonata.api.service;

import com.sonata.api.dto.MovementDTO;
import com.sonata.api.exception.NotFoundException;
import com.sonata.api.model.Movement;
import com.sonata.api.repository.MovementRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/** Regras de negocio para Movement. */
@Service
public class MovementService {

    private final MovementRepository repository;
    private final WorkService workService;

    public MovementService(MovementRepository repository, WorkService workService) {
        this.repository = repository;
        this.workService = workService;
    }

    public Movement findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Movimento " + id + " nao encontrado"));
    }

    /** Lista movimentos de uma obra na ordem correta (gapless playback). */
    public List<Movement> findByWork(Long workId) {
        return repository.findByWork_WorkIdOrderByMovementOrderAsc(workId);
    }

    public Movement create(MovementDTO dto) {
        Movement m = new Movement();
        return repository.save(apply(m, dto));
    }

    public Movement update(Long id, MovementDTO dto) {
        Movement m = findById(id);
        return repository.save(apply(m, dto));
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException("Movimento " + id + " nao encontrado");
        }
        repository.deleteById(id);
    }

    private Movement apply(Movement m, MovementDTO dto) {
        m.setTitle(dto.title());
        m.setMovementOrder(dto.movementOrder());
        m.setAudioUrl(dto.audioUrl());
        m.setDurationInSeconds(dto.durationInSeconds());
        m.setWork(workService.findById(dto.workId()));
        return m;
    }
}
