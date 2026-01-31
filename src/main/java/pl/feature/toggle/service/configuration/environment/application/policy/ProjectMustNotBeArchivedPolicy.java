package pl.feature.toggle.service.configuration.environment.application.policy;

import lombok.AllArgsConstructor;
import pl.feature.toggle.service.configuration.environment.domain.exception.CannotOperateOnEnvironmentForArchivedProjectException;
import pl.feature.toggle.service.configuration.project.application.port.out.ProjectQueryRepository;
import pl.feature.toggle.service.model.project.ProjectId;

@AllArgsConstructor
class ProjectMustNotBeArchivedPolicy {

    private final ProjectQueryRepository projectQueryRepository;

    void ensure(ProjectId projectId) {
        var projectStatus = projectQueryRepository.fetchStatus(projectId);

        if (projectStatus.isArchived()) {
            throw new CannotOperateOnEnvironmentForArchivedProjectException(projectId);
        }
    }
}
