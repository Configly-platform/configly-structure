package pl.feature.toggle.service.configuration.environment.application.port.out;

import pl.feature.toggle.service.configuration.environment.domain.Environment;
import pl.feature.toggle.service.model.environment.EnvironmentId;
import pl.feature.toggle.service.model.environment.EnvironmentName;
import pl.feature.toggle.service.model.project.ProjectId;

public interface EnvironmentQueryRepository {

    Environment getOrThrow(EnvironmentId environmentId, ProjectId projectId);

    boolean existsByProjectIdAndName(ProjectId projectId, EnvironmentName environmentName);
}
