package pl.feature.toggle.service.configuration.environment.infrastructure.in.rest.dto.internal;

import java.time.LocalDateTime;

public record EnvironmentViewResponse(
        String id,
        String projectId,
        String name,
        String type,
        String status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Long revision
) {
}
