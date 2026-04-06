package com.configly.structure.environment.application.port.in.command;

import com.configly.structure.environment.domain.EnvironmentType;
import com.configly.model.environment.EnvironmentId;
import com.configly.model.project.ProjectId;
import com.configly.web.model.actor.Actor;
import com.configly.web.model.correlation.CorrelationId;

import java.util.UUID;

public record ChangeEnvironmentTypeCommand(
        ProjectId projectId,
        EnvironmentId environmentId,
        EnvironmentType type,
        Actor actor,
        CorrelationId correlationId
) {

    public static ChangeEnvironmentTypeCommand of(UUID projectId, UUID environmentId, String type, Actor actor, CorrelationId correlationId) {
        return new ChangeEnvironmentTypeCommand(ProjectId.create(projectId), EnvironmentId.create(environmentId), EnvironmentType.valueOf(type),
                actor, correlationId);
    }

}
