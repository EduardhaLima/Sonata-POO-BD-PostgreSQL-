package com.sonata.api.controller;

import com.sonata.api.dto.MovementDTO;
import com.sonata.api.model.Movement;
import com.sonata.api.service.MovementService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/** Endpoints REST de movimentos - /api/movements. */
@RestController
@RequestMapping("/api/movements")
public class MovementController {

    private final MovementService service;

    public MovementController(MovementService service) {
        this.service = service;
    }

    /** Lista movimentos de uma obra (em ordem - garante gapless). */
    @GetMapping
    public List<Movement> listByWork(@RequestParam Long workId) {
        return service.findByWork(workId);
    }

    @GetMapping("/{id}")
    public Movement get(@PathVariable Long id) { return service.findById(id); }

    @PostMapping
    public ResponseEntity<Movement> create(@Valid @RequestBody MovementDTO dto) {
        Movement created = service.create(dto);
        return ResponseEntity.created(URI.create("/api/movements/" + created.getMovementId())).body(created);
    }

    @PutMapping("/{id}")
    public Movement update(@PathVariable Long id, @Valid @RequestBody MovementDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
