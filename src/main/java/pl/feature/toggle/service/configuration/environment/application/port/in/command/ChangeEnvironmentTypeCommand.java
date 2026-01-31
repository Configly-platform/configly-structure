package pl.feature.toggle.service.configuration.environment.application.port.in.command;

import pl.feature.toggle.service.configuration.environment.domain.EnvironmentType;
import pl.feature.toggle.service.model.environment.EnvironmentId;
import pl.feature.toggle.service.model.project.ProjectId;

import java.util.UUID;

public record ChangeEnvironmentTypeCommand(
        ProjectId projectId,
        EnvironmentId environmentId,
        EnvironmentType type
) {

    public static ChangeEnvironmentTypeCommand of(UUID projectId, UUID environmentId, String type) {
        return new ChangeEnvironmentTypeCommand(ProjectId.create(projectId), EnvironmentId.create(environmentId), EnvironmentType.valueOf(type));
    }

}
