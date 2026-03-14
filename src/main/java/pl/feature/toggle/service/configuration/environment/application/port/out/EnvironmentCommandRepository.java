package pl.feature.toggle.service.configuration.environment.application.port.out;

import pl.feature.toggle.service.configuration.environment.domain.Environment;
import pl.feature.toggle.service.configuration.environment.domain.EnvironmentUpdateResult;
import pl.feature.toggle.service.configuration.project.application.port.out.environment.CascadedEnvironmentStatusChange;
import pl.feature.toggle.service.model.environment.EnvironmentId;
import pl.feature.toggle.service.model.project.ProjectId;

import java.util.List;

public interface EnvironmentCommandRepository {

    EnvironmentId save(Environment environment);

    List<CascadedEnvironmentStatusChange> archiveAllByProjectId(ProjectId projectId);

    void update(EnvironmentUpdateResult updateResult);
}
