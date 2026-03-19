package pl.feature.toggle.service.configuration.project.application.port.in.command;

import pl.feature.toggle.service.configuration.project.infrastructure.in.rest.dto.ProjectSnapshotDto;
import pl.feature.toggle.service.model.project.ProjectDescription;
import pl.feature.toggle.service.model.project.ProjectName;
import pl.feature.toggle.service.web.actor.Actor;
import pl.feature.toggle.service.web.correlation.CorrelationId;

public record CreateProjectCommand(
        ProjectName name,
        ProjectDescription description,
        Actor actor,
        CorrelationId correlationId
) {

    public static CreateProjectCommand from(ProjectSnapshotDto dto, Actor actor, CorrelationId correlationId) {
        return new CreateProjectCommand(ProjectName.create(dto.name()), ProjectDescription.create(dto.description()),
                actor, correlationId);
    }

}
