package com.sonata.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/** DTO para criar/atualizar um Movement. */
public record MovementDTO(
        @NotBlank String title,
        @NotNull @Positive Integer movementOrder,
        String audioUrl,
        Integer durationInSeconds,
        @NotNull Long workId
) { }
