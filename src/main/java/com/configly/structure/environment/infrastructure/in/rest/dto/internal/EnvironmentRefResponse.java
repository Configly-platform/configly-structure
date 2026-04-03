package com.configly.structure.environment.infrastructure.in.rest.dto.internal;

public record EnvironmentRefResponse(
        String environmentId,
        String projectId,
        String status,
        Long lastRevision
) {
}
