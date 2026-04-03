package com.configly.structure.environment.application.port.in.command;

import com.configly.structure.environment.domain.EnvironmentType;
import com.configly.structure.environment.infrastructure.in.rest.dto.CreateEnvironmentDto;
import com.configly.model.environment.EnvironmentName;
import com.configly.model.project.ProjectId;
import com.configly.web.actor.Actor;
import com.configly.web.correlation.CorrelationId;

import java.util.UUID;

public record CreateEnvironmentCommand(
        EnvironmentName name,
        ProjectId projectId,
        EnvironmentType type,
        Actor actor,
        CorrelationId correlationId
) {

    public static CreateEnvironmentCommand from(UUID projectId, CreateEnvironmentDto dto, Actor actor, CorrelationId correlationId) {
        return new CreateEnvironmentCommand(EnvironmentName.create(dto.name()), ProjectId.create(projectId), EnvironmentType.valueOf(dto.type()),
                actor, correlationId);
    }
}
