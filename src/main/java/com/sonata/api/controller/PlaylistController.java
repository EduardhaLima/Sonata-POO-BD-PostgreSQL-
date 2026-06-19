package com.sonata.api.controller;

import com.sonata.api.dto.PlaylistDTO;
import com.sonata.api.model.Playlist;
import com.sonata.api.service.PlaylistService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/** Endpoints REST de playlists - /api/playlists. */
@RestController
@RequestMapping("/api/playlists")
public class PlaylistController {

    private final PlaylistService service;

    public PlaylistController(PlaylistService service) {
        this.service = service;
    }

    @GetMapping
    public List<Playlist> list(@RequestParam(required = false) Long userId) {
        return (userId == null) ? service.findAll() : service.findByUser(userId);
    }

    @GetMapping("/{id}")
    public Playlist get(@PathVariable Long id) { return service.findById(id); }

    @PostMapping
    public ResponseEntity<Playlist> create(@Valid @RequestBody PlaylistDTO dto) {
        Playlist created = service.create(dto);
        return ResponseEntity.created(URI.create("/api/playlists/" + created.getPlaylistId())).body(created);
    }

    /** Adiciona um movimento na playlist. */
    @PostMapping("/{playlistId}/movements/{movementId}")
    public Playlist addMovement(@PathVariable Long playlistId, @PathVariable Long movementId) {
        return service.addMovement(playlistId, movementId);
    }

    @DeleteMapping("/{playlistId}/movements/{movementId}")
    public Playlist removeMovement(@PathVariable Long playlistId, @PathVariable Long movementId) {
        return service.removeMovement(playlistId, movementId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
