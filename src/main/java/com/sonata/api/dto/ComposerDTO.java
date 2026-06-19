package com.sonata.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** DTO para criar/atualizar um Composer. */
public record ComposerDTO(
        @NotBlank @Size(max = 255) String name,
        String historicalPeriod,
        String nationality,
        String biography
) { }
