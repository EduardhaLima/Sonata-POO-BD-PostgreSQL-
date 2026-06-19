package com.sonata.api.controller;

import com.sonata.api.dto.WorkDTO;
import com.sonata.api.model.Work;
import com.sonata.api.service.WorkService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/** Endpoints REST de obras - /api/works. */
@RestController
@RequestMapping("/api/works")
public class WorkController {

    private final WorkService service;

    public WorkController(WorkService service) {
        this.service = service;
    }

    @GetMapping
    public List<Work> list(@RequestParam(required = false) Long composerId) {
        return (composerId == null) ? service.findAll() : service.findByComposer(composerId);
    }

    @GetMapping("/{id}")
    public Work get(@PathVariable Long id) { return service.findById(id); }

    @PostMapping
    public ResponseEntity<Work> create(@Valid @RequestBody WorkDTO dto) {
        Work created = service.create(dto);
        return ResponseEntity.created(URI.create("/api/works/" + created.getWorkId())).body(created);
    }

    @PutMapping("/{id}")
    public Work update(@PathVariable Long id, @Valid @RequestBody WorkDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
