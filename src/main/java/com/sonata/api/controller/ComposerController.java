package com.sonata.api.controller;

import com.sonata.api.dto.ComposerDTO;
import com.sonata.api.model.Composer;
import com.sonata.api.service.ComposerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/** Endpoints REST de compositores - /api/composers. */
@RestController
@RequestMapping("/api/composers")
public class ComposerController {

    private final ComposerService service;

    public ComposerController(ComposerService service) {
        this.service = service;
    }

    @GetMapping
    public List<Composer> list(@RequestParam(required = false) String period) {
        return (period == null) ? service.findAll() : service.findByPeriod(period);
    }

    @GetMapping("/{id}")
    public Composer get(@PathVariable Long id) { return service.findById(id); }

    @PostMapping
    public ResponseEntity<Composer> create(@Valid @RequestBody ComposerDTO dto) {
        Composer created = service.create(dto);
        return ResponseEntity.created(URI.create("/api/composers/" + created.getComposerId())).body(created);
    }

    @PutMapping("/{id}")
    public Composer update(@PathVariable Long id, @Valid @RequestBody ComposerDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
