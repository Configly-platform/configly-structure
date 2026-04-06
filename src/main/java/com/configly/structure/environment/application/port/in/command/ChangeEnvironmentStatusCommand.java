package com.configly.structure.environment.application.port.in.command;

import com.configly.model.environment.EnvironmentId;
import com.configly.model.environment.EnvironmentStatus;
import com.configly.model.project.ProjectId;
import com.configly.web.model.actor.Actor;
import com.configly.web.model.correlation.CorrelationId;

import java.util.UUID;

public record ChangeEnvironmentStatusCommand(
        ProjectId projectId,
        EnvironmentId environmentId,
        EnvironmentStatus newEnvironmentStatus,
        Actor actor,
        CorrelationId correlationId
) {

    public static ChangeEnvironmentStatusCommand of(UUID projectId, UUID environmentId, String status, Actor actor, CorrelationId correlationId) {
        return new ChangeEnvironmentStatusCommand(ProjectId.create(projectId), EnvironmentId.create(environmentId), EnvironmentStatus.valueOf(status),
                actor, correlationId);
    }

}
