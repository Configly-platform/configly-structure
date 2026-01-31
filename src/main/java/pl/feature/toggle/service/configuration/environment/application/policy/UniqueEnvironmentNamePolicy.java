package pl.feature.toggle.service.configuration.environment.application.policy;

import lombok.AllArgsConstructor;
import pl.feature.toggle.service.configuration.environment.application.port.out.EnvironmentQueryRepository;
import pl.feature.toggle.service.configuration.environment.domain.exception.EnvironmentAlreadyExistsException;
import pl.feature.toggle.service.model.environment.EnvironmentName;
import pl.feature.toggle.service.model.project.ProjectId;

@AllArgsConstructor
class UniqueEnvironmentNamePolicy {

    private final EnvironmentQueryRepository environmentQueryRepository;

    void ensure(ProjectId projectId, EnvironmentName environmentName) {
        var existsByProjectIdAndName = environmentQueryRepository.existsByProjectIdAndName(projectId, environmentName);
        if (existsByProjectIdAndName) {
            throw new EnvironmentAlreadyExistsException(environmentName, projectId);
        }
    }
}
