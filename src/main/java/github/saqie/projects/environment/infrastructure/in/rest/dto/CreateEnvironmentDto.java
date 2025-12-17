package github.saqie.projects.environment.infrastructure.in.rest.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateEnvironmentDto(
        @NotEmpty
        String name,
        @NotNull
        UUID projectId
) {
}
