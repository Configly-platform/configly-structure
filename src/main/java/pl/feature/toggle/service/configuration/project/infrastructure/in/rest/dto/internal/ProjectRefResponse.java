package pl.feature.toggle.service.configuration.project.infrastructure.in.rest.dto.internal;

public record ProjectRefResponse(
        String projectId,
        String status,
        Long revision
) {
}
