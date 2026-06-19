package com.sonata.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/** DTO para criar uma Playlist. */
public record PlaylistDTO(
        @NotBlank String name,
        @NotNull Long userId
) { }
