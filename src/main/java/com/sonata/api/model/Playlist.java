package com.sonata.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Entidade Playlist - colecao pessoal de movimentos montada pelo usuario.
 *
 * <p>Relacao N:N com {@link Movement} via tabela <code>playlist_movement</code>.
 * Usamos {@link Set} (LinkedHashSet) para:
 * <ul>
 *   <li>impedir duplicatas (regra de negocio do projeto);</li>
 *   <li>preservar a ordem de insercao.</li>
 * </ul>
 * Limite: ate 100 movimentos por playlist (validado no service).</p>
 */
@Entity
@Table(name = "playlist")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Playlist {

    public static final int MAX_MOVEMENTS = 100;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "playlist_id")
    private Long playlistId;

    @NotBlank
    @Size(max = 150)
    @Column(nullable = false, length = 150)
    private String name;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /** Dono da playlist. */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /** Movimentos da playlist (N:N). */
    @ManyToMany
    @JoinTable(
            name = "playlist_movement",
            joinColumns = @JoinColumn(name = "playlist_id"),
            inverseJoinColumns = @JoinColumn(name = "movement_id")
    )
    @Builder.Default
    private Set<Movement> movements = new LinkedHashSet<>();

    /** Define created_at automaticamente na persistencia. */
    @PrePersist
    void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }
}
