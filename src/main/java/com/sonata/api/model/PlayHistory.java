package com.sonata.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

/**
 * PlayHistory (Historico de Reproducao).
 *
 * <p>Registra cada vez que um {@link User} toca um {@link Movement}.
 * Base para funcionalidades como:
 * <ul>
 *   <li>"Tocados recentemente"</li>
 *   <li>Estatisticas de escuta</li>
 *   <li>Recomendacoes por compositor/periodo</li>
 * </ul>
 */
@Entity
@Table(
    name = "play_history",
    indexes = {
        @Index(name = "idx_history_user_time", columnList = "user_id, played_at")
    }
)
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class PlayHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id")
    private Long historyId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movement_id", nullable = false)
    private Movement movement;

    /** Momento em que a reproducao comecou. */
    @NotNull
    @Column(name = "played_at", nullable = false)
    private LocalDateTime playedAt;

    /** Quantos segundos o usuario realmente ouviu (para metricas). */
    @Column(name = "listened_seconds")
    private Integer listenedSeconds;

    @PrePersist
    void onCreate() {
        if (this.playedAt == null) {
            this.playedAt = LocalDateTime.now();
        }
    }
}
