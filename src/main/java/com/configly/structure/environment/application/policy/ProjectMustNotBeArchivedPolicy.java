package com.configly.structure.environment.application.policy;

import lombok.AllArgsConstructor;
import com.configly.structure.environment.domain.exception.CannotOperateOnEnvironmentForArchivedProjectException;
import com.configly.structure.project.application.port.out.ProjectQueryRepository;
import com.configly.model.project.ProjectId;

@AllArgsConstructor
class ProjectMustNotBeArchivedPolicy {

    private final ProjectQueryRepository projectQueryRepository;

    static ProjectMustNotBeArchivedPolicy create(ProjectQueryRepository projectQueryRepository) {
        return new ProjectMustNotBeArchivedPolicy(projectQueryRepository);
    }

    void ensure(ProjectId projectId) {
        var projectStatus = projectQueryRepository.fetchStatus(projectId);

        if (projectStatus.isArchived()) {
            throw new CannotOperateOnEnvironmentForArchivedProjectException(projectId);
        }
    }
}
