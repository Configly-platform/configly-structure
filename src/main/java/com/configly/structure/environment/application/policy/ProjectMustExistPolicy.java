package com.configly.structure.environment.application.policy;

import lombok.AllArgsConstructor;
import com.configly.structure.environment.domain.exception.CannotCreateEnvironmentForMissingProjectException;
import com.configly.structure.project.application.port.out.ProjectQueryRepository;
import com.configly.model.project.ProjectId;

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
