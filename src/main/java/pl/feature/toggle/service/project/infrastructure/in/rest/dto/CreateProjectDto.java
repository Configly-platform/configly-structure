package pl.feature.toggle.service.project.infrastructure.in.rest.dto;

import jakarta.validation.constraints.NotEmpty;

public record CreateProjectDto(
        @NotEmpty
        String name,
        @NotEmpty
        String description
) {
}
