package com.sonata.api.service;

import com.sonata.api.model.PlayHistory;
import com.sonata.api.repository.PlayHistoryRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

/** Regras de negocio para PlayHistory . */
@Service
public class PlayHistoryService {

    private final PlayHistoryRepository repository;
    private final UserService userService;
    private final MovementService movementService;

    public PlayHistoryService(PlayHistoryRepository repository,
                              UserService userService,
                              MovementService movementService) {
        this.repository = repository;
        this.userService = userService;
        this.movementService = movementService;
    }

    /** Registra que o usuario tocou determinado movimento. */
    public PlayHistory register(Long userId, Long movementId, Integer listenedSeconds) {
        PlayHistory h = PlayHistory.builder()
                .user(userService.findById(userId))
                .movement(movementService.findById(movementId))
                .listenedSeconds(listenedSeconds)
                .build();
        return repository.save(h);
    }

    /** Ultimas N reproducoes do usuario. */
    public List<PlayHistory> recent(Long userId, int limit) {
        return repository.findByUser_UserIdOrderByPlayedAtDesc(userId, PageRequest.of(0, limit));
    }
}
