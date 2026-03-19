package pl.feature.toggle.service.configuration.environment.application.port.in.command;

import pl.feature.toggle.service.configuration.environment.domain.EnvironmentType;
import pl.feature.toggle.service.configuration.environment.infrastructure.in.rest.dto.CreateEnvironmentDto;
import pl.feature.toggle.service.model.environment.EnvironmentName;
import pl.feature.toggle.service.model.project.ProjectId;
import pl.feature.toggle.service.web.actor.Actor;
import pl.feature.toggle.service.web.correlation.CorrelationId;

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
