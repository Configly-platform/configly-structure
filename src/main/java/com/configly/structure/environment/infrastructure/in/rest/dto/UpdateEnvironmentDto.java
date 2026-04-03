package com.configly.structure.environment.infrastructure.in.rest.dto;

import jakarta.validation.constraints.NotEmpty;

public record UpdateEnvironmentDto(
        @NotEmpty
        String name
) {
}
