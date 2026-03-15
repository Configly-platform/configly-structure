package pl.feature.toggle.service.configuration.environment.application.port.in.command;

import pl.feature.toggle.service.model.environment.EnvironmentId;
import pl.feature.toggle.service.model.environment.EnvironmentStatus;
import pl.feature.toggle.service.model.project.ProjectId;
import pl.feature.toggle.service.model.security.actor.Actor;
import pl.feature.toggle.service.model.security.correlation.CorrelationId;

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
