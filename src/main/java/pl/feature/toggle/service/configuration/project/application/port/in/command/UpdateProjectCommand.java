package pl.feature.toggle.service.configuration.project.application.port.in.command;

import pl.feature.toggle.service.configuration.project.infrastructure.in.rest.dto.ProjectSnapshotDto;
import pl.feature.toggle.service.model.project.ProjectDescription;
import pl.feature.toggle.service.model.project.ProjectId;
import pl.feature.toggle.service.model.project.ProjectName;
import pl.feature.toggle.service.model.security.actor.Actor;
import pl.feature.toggle.service.model.security.correlation.CorrelationId;

public record UpdateProjectCommand(
        ProjectId projectId,
        ProjectName name,
        ProjectDescription description,
        Actor actor,
        CorrelationId correlationId
) {

    public static UpdateProjectCommand from(String projectId, ProjectSnapshotDto dto, Actor actor, CorrelationId correlationId) {
        return new UpdateProjectCommand(ProjectId.create(projectId), ProjectName.create(dto.name()), ProjectDescription.create(dto.description()),
                actor, correlationId);
    }
}
