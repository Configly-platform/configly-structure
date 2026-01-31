package pl.feature.toggle.service.configuration.environment.domain.exception;

import pl.feature.toggle.service.model.environment.EnvironmentId;
import pl.feature.toggle.service.model.project.ProjectId;

public class EnvironmentNotAssignedToProjectException extends RuntimeException {
    public EnvironmentNotAssignedToProjectException(EnvironmentId environmentId, ProjectId projectId) {
        super("Environment: [" + environmentId.idAsString() + "] is not assigned to project: [" + projectId.idAsString() + "]");
    }
}
