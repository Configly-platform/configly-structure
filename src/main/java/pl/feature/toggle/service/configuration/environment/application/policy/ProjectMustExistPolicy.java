package pl.feature.toggle.service.configuration.environment.application.policy;

import lombok.AllArgsConstructor;
import pl.feature.toggle.service.configuration.environment.domain.exception.CannotCreateEnvironmentForMissingProjectException;
import pl.feature.toggle.service.configuration.project.application.port.out.ProjectQueryRepository;
import pl.feature.toggle.service.model.project.ProjectId;

@AllArgsConstructor
class ProjectMustExistPolicy {

    private final ProjectQueryRepository projectQueryRepository;

    static ProjectMustExistPolicy create(ProjectQueryRepository projectQueryRepository) {
        return new ProjectMustExistPolicy(projectQueryRepository);
    }

    void ensure(ProjectId projectId) {
        var exists = projectQueryRepository.exists(projectId);
        if (!exists) {
            throw new CannotCreateEnvironmentForMissingProjectException(projectId);
        }
    }
}
