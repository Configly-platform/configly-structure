package pl.feature.toggle.service.configuration.environment.application.port.in;

import pl.feature.toggle.service.configuration.environment.infrastructure.in.rest.dto.CreateEnvironmentDto;
import pl.feature.toggle.service.model.environment.EnvironmentName;
import pl.feature.toggle.service.model.project.ProjectId;

import java.util.UUID;

public record CreateEnvironmentCommand(
        EnvironmentName name,
        ProjectId projectId
) {

    public static CreateEnvironmentCommand from(CreateEnvironmentDto dto) {
        return new CreateEnvironmentCommand(EnvironmentName.create(dto.name()), ProjectId.create(dto.projectId()));
    }

    public static CreateEnvironmentCommand create(String name, UUID projectId) {
        return new CreateEnvironmentCommand(EnvironmentName.create(name), ProjectId.create(projectId));
    }

}
