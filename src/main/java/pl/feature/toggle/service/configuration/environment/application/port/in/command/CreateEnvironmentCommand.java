package pl.feature.toggle.service.configuration.environment.application.port.in.command;

import pl.feature.toggle.service.configuration.environment.domain.EnvironmentType;
import pl.feature.toggle.service.configuration.environment.infrastructure.in.rest.dto.CreateEnvironmentDto;
import pl.feature.toggle.service.model.environment.EnvironmentName;
import pl.feature.toggle.service.model.project.ProjectId;

import java.util.UUID;

public record CreateEnvironmentCommand(
        EnvironmentName name,
        ProjectId projectId,
        EnvironmentType type
) {

    public static CreateEnvironmentCommand from(UUID projectId, CreateEnvironmentDto dto) {
        return new CreateEnvironmentCommand(EnvironmentName.create(dto.name()), ProjectId.create(projectId), EnvironmentType.valueOf(dto.type()));
    }
}
