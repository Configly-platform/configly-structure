package pl.feature.toggle.service.configuration.project.infrastructure.in.rest.dto;

import jakarta.validation.constraints.NotEmpty;

public record ProjectSnapshotDto(
        @NotEmpty
        String name,
        @NotEmpty
        String description
) {
}
