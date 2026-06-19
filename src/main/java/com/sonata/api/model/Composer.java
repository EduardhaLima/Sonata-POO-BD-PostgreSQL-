package com.sonata.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidade Composer - representa um compositor de musica classica
 * (Beethoven, Mozart, Bach, etc.).
 *
 * <p>Hierarquia do catalogo: Composer 1 ---- N Work 1 ---- N Movement.</p>
 */
@Entity
@Table(name = "composer")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Composer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "composer_id")
    private Long composerId;

    @NotBlank
    @Size(max = 255)
    @Column(nullable = false, length = 255)
    private String name;

    /** Periodo historico: Barroco, Classico, Romantico, Moderno... */
    @Column(name = "historical_period", length = 50)
    private String historicalPeriod;

    @Column(length = 100)
    private String nationality;

    /** Biografia em texto livre (pode ser longa). */
    @Lob
    @Column(columnDefinition = "TEXT")
    private String biography;

    /** Obras compostas por este compositor. */
    @OneToMany(mappedBy = "composer", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Work> works = new ArrayList<>();

    /** Helper de dominio - lista as obras do compositor. */
    public List<Work> listWorks() {
        return this.works;
    }
}
