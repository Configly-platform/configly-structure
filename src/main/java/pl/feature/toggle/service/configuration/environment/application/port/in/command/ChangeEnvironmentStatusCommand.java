package pl.feature.toggle.service.configuration.environment.application.port.in.command;

import pl.feature.toggle.service.model.environment.EnvironmentId;
import pl.feature.toggle.service.model.environment.EnvironmentStatus;
import pl.feature.toggle.service.model.project.ProjectId;

import java.util.UUID;

public record ChangeEnvironmentStatusCommand(
        ProjectId projectId,
        EnvironmentId environmentId,
        EnvironmentStatus newEnvironmentStatus
) {

    public static ChangeEnvironmentStatusCommand of(UUID projectId, UUID environmentId, String status) {
        return new ChangeEnvironmentStatusCommand(ProjectId.create(projectId), EnvironmentId.create(environmentId), EnvironmentStatus.valueOf(status));
    }

}
