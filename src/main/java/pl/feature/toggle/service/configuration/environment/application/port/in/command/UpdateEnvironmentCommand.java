package pl.feature.toggle.service.configuration.environment.application.port.in.command;

import pl.feature.toggle.service.configuration.environment.infrastructure.in.rest.dto.UpdateEnvironmentDto;
import pl.feature.toggle.service.model.environment.EnvironmentId;
import pl.feature.toggle.service.model.environment.EnvironmentName;
import pl.feature.toggle.service.model.project.ProjectId;

import java.util.UUID;

public record UpdateEnvironmentCommand(
        EnvironmentId environmentId,
        ProjectId projectId,
        EnvironmentName name
) {

    public static UpdateEnvironmentCommand of(UUID projectId, UUID environmentId, UpdateEnvironmentDto dto) {
        return new UpdateEnvironmentCommand(EnvironmentId.create(environmentId), ProjectId.create(projectId), EnvironmentName.create(dto.name()));
    }

}
