package com.sonata.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO de criacao/atualizacao de {@link com.sonata.api.model.User}.
 * Evita expor a entidade JPA diretamente nos endpoints.
 */
public record UserDTO(
        @NotBlank @Size(max = 255) String name,
        @NotBlank @Email @Size(max = 150) String email,
        @NotBlank @Size(min = 6, max = 100) String password,
        String preferredAudioQuality
) { }
