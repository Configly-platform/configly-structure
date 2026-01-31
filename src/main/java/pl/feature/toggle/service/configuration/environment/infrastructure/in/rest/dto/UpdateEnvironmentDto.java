package pl.feature.toggle.service.configuration.environment.infrastructure.in.rest.dto;

import jakarta.validation.constraints.NotEmpty;

public record UpdateEnvironmentDto(
        @NotEmpty
        String name
) {
}
