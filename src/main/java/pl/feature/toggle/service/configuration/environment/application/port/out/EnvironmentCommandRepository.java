package pl.feature.toggle.service.configuration.environment.application.port.out;

import pl.feature.toggle.service.configuration.environment.domain.Environment;
import pl.feature.toggle.service.configuration.environment.domain.EnvironmentUpdateResult;
import pl.feature.toggle.service.model.environment.EnvironmentId;
import pl.feature.toggle.service.model.project.ProjectId;

public interface EnvironmentCommandRepository {

    EnvironmentId save(Environment environment);

    void archiveAllByProjectId(ProjectId projectId);

    void restoreAllByProjectId(ProjectId projectId);

    void update(EnvironmentUpdateResult updateResult);
}
