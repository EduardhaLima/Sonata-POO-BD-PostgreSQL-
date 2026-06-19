package com.sonata.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidade User - representa um ouvinte do Sonata.
 *
 * <p>Mapeada na tabela <code>users</code> (evita conflito com a palavra
 * reservada "user" do SQL Server).</p>
 *
 * <p>Relacoes:
 * <ul>
 *   <li>1:N com {@link Playlist} - um usuario pode ter varias playlists</li>
 *   <li>1:1 com {@link Subscription} - um usuario tem um plano corrente</li>
 *   <li>1:N com {@link PlayHistory} - historico de movimentos tocados</li>
 * </ul>
 */
@Entity
@Table(name = "users")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class User {

    /** Chave primaria gerada pelo banco (IDENTITY no SQL Server). */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @NotBlank
    @Size(max = 255)
    @Column(nullable = false, length = 255)
    private String name;

    /** Email unico - usado como login. */
    @NotBlank
    @Email
    @Size(max = 150)
    @Column(nullable = false, unique = true, length = 150)
    private String email;

    /** Senha armazenada SEMPRE com hash BCrypt - nunca em texto puro. */
    @NotBlank
    @Size(max = 255)
    @Column(nullable = false, length = 255)
    private String password;

    /**
     * Qualidade de audio preferida pelo usuario (NORMAL / HIGH / LOSSLESS).
     * Atende ao requisito nao funcional de qualidade FLAC do Sonata.
     */
    @Column(name = "preferred_audio_quality", length = 20)
    private String preferredAudioQuality;

    /** Playlists pertencentes ao usuario. */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Playlist> playlists = new ArrayList<>();

    /**
     * Metodo de dominio - simula autenticacao basica.
     * A logica real (comparar hash BCrypt) fica no SecurityConfig/Service.
     */
    public boolean authenticate(String rawPassword) {
        return this.password != null && !this.password.isBlank();
    }
}
