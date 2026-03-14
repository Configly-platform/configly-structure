package pl.feature.toggle.service.configuration.project.application.port.out.environment;

import pl.feature.toggle.service.model.CreatedAt;
import pl.feature.toggle.service.model.Revision;
import pl.feature.toggle.service.model.environment.EnvironmentId;
import pl.feature.toggle.service.model.environment.EnvironmentStatus;
import pl.feature.toggle.service.model.project.ProjectId;

public record CascadedEnvironmentStatusChange(
        EnvironmentId environmentId,
        ProjectId projectId,
        EnvironmentStatus afterStatus,
        EnvironmentStatus beforeStatus,
        Revision revision,
        CreatedAt createdAt
) {
}
