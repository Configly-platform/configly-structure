package com.configly.structure.environment.application.port.in.command;

import com.configly.structure.environment.infrastructure.in.rest.dto.UpdateEnvironmentDto;
import com.configly.model.environment.EnvironmentId;
import com.configly.model.environment.EnvironmentName;
import com.configly.model.project.ProjectId;
import com.configly.web.model.actor.Actor;
import com.configly.web.model.correlation.CorrelationId;

import java.util.UUID;

public record UpdateEnvironmentCommand(
        EnvironmentId environmentId,
        ProjectId projectId,
        EnvironmentName name,
        Actor actor,
        CorrelationId correlationId
) {

    public static UpdateEnvironmentCommand of(UUID projectId, UUID environmentId, UpdateEnvironmentDto dto, Actor actor, CorrelationId correlationId) {
        return new UpdateEnvironmentCommand(EnvironmentId.create(environmentId), ProjectId.create(projectId), EnvironmentName.create(dto.name()),
                actor, correlationId);
    }

}
