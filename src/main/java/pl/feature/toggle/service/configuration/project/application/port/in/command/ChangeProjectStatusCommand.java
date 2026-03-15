package pl.feature.toggle.service.configuration.project.application.port.in.command;

import pl.feature.toggle.service.model.project.ProjectId;
import pl.feature.toggle.service.model.project.ProjectStatus;
import pl.feature.toggle.service.model.security.actor.Actor;
import pl.feature.toggle.service.model.security.correlation.CorrelationId;

public record ChangeProjectStatusCommand(
        ProjectId projectId,
        ProjectStatus newProjectStatus,
        Actor actor,
        CorrelationId correlationId
) {
    public static ChangeProjectStatusCommand of(String projectId, String status, Actor actor, CorrelationId correlationId) {
        return new ChangeProjectStatusCommand(ProjectId.create(projectId), ProjectStatus.valueOf(status),
                actor, correlationId);
    }
}
