package pl.feature.toggle.service.configuration.environment.infrastructure.in.rest.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateEnvironmentDto(
        @NotEmpty
        String name,
        @NotEmpty
        String type
) {
}
