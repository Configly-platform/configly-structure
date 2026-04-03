package com.configly.structure.project.infrastructure.in.rest.dto.internal;

import java.time.LocalDateTime;

public record ProjectViewResponse(
        String id,
        String name,
        String description,
        String status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Long revision
) {
}
