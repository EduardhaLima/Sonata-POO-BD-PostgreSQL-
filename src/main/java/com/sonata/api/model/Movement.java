package com.sonata.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

/**
 * Entidade Movement - representa um MOVIMENTO de uma obra
 * (ex.: "I. Allegro con brio" da Sinfonia nº 5).
 *
 * <p>Eh a unidade minima de reproducao do Sonata: o "play" toca um
 * Movement, nao uma Work inteira.</p>
 */
@Entity
@Table(name = "movement")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Movement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movement_id")
    private Long movementId;

    @NotBlank
    @Column(nullable = false, length = 255)
    private String title;

    /** Ordem do movimento dentro da obra - sustenta o gapless playback. */
    @NotNull
    @Positive
    @Column(name = "movement_order", nullable = false)
    private Integer movementOrder;

    /** URL do arquivo de audio (FLAC/MP3) - apontara para storage externo. */
    @Column(name = "audio_url", length = 500)
    private String audioUrl;

    /** Duracao do movimento em segundos. */
    @Column(name = "duration_in_seconds")
    private Integer durationInSeconds;

    /** Obra a qual o movimento pertence (N:1). */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_id", nullable = false)
    private Work work;

    /**
     * Stub de dominio - a reproducao real eh disparada pelo client (player)
     * apos receber a URL via endpoint.
     */
    public void playAudio() {
        // No-op no backend - serve como contrato de dominio.
    }
}
