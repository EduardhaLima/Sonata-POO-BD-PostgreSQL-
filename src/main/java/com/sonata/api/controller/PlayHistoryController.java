package com.sonata.api.controller;

import com.sonata.api.model.PlayHistory;
import com.sonata.api.service.PlayHistoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** Endpoints REST de historico de reproducao  - /api/history. */
@RestController
@RequestMapping("/api/history")
public class PlayHistoryController {

    private final PlayHistoryService service;

    public PlayHistoryController(PlayHistoryService service) {
        this.service = service;
    }

    /** Registra uma reproducao. */
    @PostMapping
    public PlayHistory register(@RequestParam Long userId,
                                @RequestParam Long movementId,
                                @RequestParam(required = false) Integer listenedSeconds) {
        return service.register(userId, movementId, listenedSeconds);
    }

    /** Tocados recentemente pelo usuario (default 20). */
    @GetMapping("/user/{userId}")
    public List<PlayHistory> recent(@PathVariable Long userId,
                                    @RequestParam(defaultValue = "20") int limit) {
        return service.recent(userId, limit);
    }
}
