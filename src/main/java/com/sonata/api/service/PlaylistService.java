package com.sonata.api.service;

import com.sonata.api.dto.PlaylistDTO;
import com.sonata.api.exception.BusinessException;
import com.sonata.api.exception.NotFoundException;
import com.sonata.api.model.Movement;
import com.sonata.api.model.Playlist;
import com.sonata.api.repository.PlaylistRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Regras de negocio para Playlist.
 *
 * <p>Aplica as regras pedidas na documentacao:
 * <ul>
 *   <li>maximo de {@link Playlist#MAX_MOVEMENTS} movimentos;</li>
 *   <li>nao permite movimento duplicado (Set ja resolve, mas validamos a mensagem).</li>
 * </ul>
 */
@Service
public class PlaylistService {

    private final PlaylistRepository repository;
    private final UserService userService;
    private final MovementService movementService;

    public PlaylistService(PlaylistRepository repository,
                           UserService userService,
                           MovementService movementService) {
        this.repository = repository;
        this.userService = userService;
        this.movementService = movementService;
    }

    public List<Playlist> findAll() { return repository.findAll(); }

    public Playlist findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Playlist " + id + " nao encontrada"));
    }

    public List<Playlist> findByUser(Long userId) {
        return repository.findByUser_UserId(userId);
    }

    public Playlist create(PlaylistDTO dto) {
        Playlist p = Playlist.builder()
                .name(dto.name())
                .user(userService.findById(dto.userId()))
                .build();
        return repository.save(p);
    }

    /** Adiciona um movimento na playlist respeitando limite e duplicidade. */
    public Playlist addMovement(Long playlistId, Long movementId) {
        Playlist playlist = findById(playlistId);
        Movement movement = movementService.findById(movementId);

        if (playlist.getMovements().contains(movement)) {
            throw new BusinessException("Movimento ja existe na playlist");
        }
        if (playlist.getMovements().size() >= Playlist.MAX_MOVEMENTS) {
            throw new BusinessException(
                    "Playlist atingiu o limite de " + Playlist.MAX_MOVEMENTS + " movimentos");
        }
        playlist.getMovements().add(movement);
        return repository.save(playlist);
    }

    public Playlist removeMovement(Long playlistId, Long movementId) {
        Playlist playlist = findById(playlistId);
        Movement movement = movementService.findById(movementId);
        playlist.getMovements().remove(movement);
        return repository.save(playlist);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException("Playlist " + id + " nao encontrada");
        }
        repository.deleteById(id);
    }
}
