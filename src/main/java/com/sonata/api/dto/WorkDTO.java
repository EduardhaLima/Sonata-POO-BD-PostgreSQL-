package com.sonata.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/** DTO para criar/atualizar uma Work (obra). opusNumber eh obrigatorio. */
public record WorkDTO(
        @NotBlank String title,
        @NotBlank String opusNumber,
        Integer compositionYear,
        String instrumentation,
        @NotNull Long composerId
) { }
