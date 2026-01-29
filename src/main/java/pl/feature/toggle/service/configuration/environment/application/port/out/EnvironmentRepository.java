package pl.feature.toggle.service.configuration.environment.application.port.out;

import pl.feature.toggle.service.configuration.environment.domain.Environment;
import pl.feature.toggle.service.model.environment.EnvironmentId;
import pl.feature.toggle.service.model.environment.EnvironmentName;
import pl.feature.toggle.service.model.project.ProjectId;

import java.util.Optional;

public interface EnvironmentRepository {

    EnvironmentId save(Environment environment);

    Optional<Environment> findById(EnvironmentId environmentId);

    boolean existsByProjectIdAndName(ProjectId projectId, EnvironmentName environmentName);

    boolean exists(EnvironmentId environmentId);

    void archiveAllByProjectId(ProjectId projectId);

    void restoreAllByProjectId(ProjectId projectId);
}
