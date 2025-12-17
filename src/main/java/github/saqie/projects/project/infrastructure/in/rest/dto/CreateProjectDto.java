package github.saqie.projects.project.infrastructure.in.rest.dto;

import jakarta.validation.constraints.NotEmpty;

public record CreateProjectDto(
        @NotEmpty
        String name,
        @NotEmpty
        String description
) {
}
