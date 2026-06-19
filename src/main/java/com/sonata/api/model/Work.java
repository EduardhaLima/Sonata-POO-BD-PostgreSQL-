package com.sonata.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidade Work - representa uma OBRA musical (ex.: "Sinfonia nº 5 em Do Menor").
 *
 * <p>Regra de negocio chave do Sonata: <b>opus_number eh OBRIGATORIO</b>.
 * Esse eh um dos diferenciais tecnicos do catalogo erudito do projeto.</p>
 */
@Entity
@Table(name = "work")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Work {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "work_id")
    private Long workId;

    @NotBlank
    @Size(max = 255)
    @Column(nullable = false, length = 255)
    private String title;

    /** Numero de Opus - OBRIGATORIO (diferencial do Sonata). */
    @NotBlank
    @Size(max = 50)
    @Column(name = "opus_number", nullable = false, length = 50)
    private String opusNumber;

    @Column(name = "composition_year")
    private Integer compositionYear;

    /** Ex.: "Orquestra Sinfonica", "Quarteto de Cordas". */
    @Column(length = 255)
    private String instrumentation;

    /** Compositor da obra (N:1). */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "composer_id", nullable = false)
    private Composer composer;

    /**
     * Movimentos que compoem a obra. A ORDEM importa: eh por ela que o
     * sistema garante <i>gapless playback</i> (reproducao sem pausa).
     */
    @OneToMany(mappedBy = "work", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("movementOrder ASC")
    @Builder.Default
    private List<Movement> movements = new ArrayList<>();

    /** Calcula a duracao total da obra (soma dos movimentos), em segundos. */
    public int calculateDuration() {
        return movements.stream()
                .mapToInt(m -> m.getDurationInSeconds() == null ? 0 : m.getDurationInSeconds())
                .sum();
    }
}
