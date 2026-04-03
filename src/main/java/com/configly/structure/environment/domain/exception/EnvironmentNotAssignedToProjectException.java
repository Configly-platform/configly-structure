package com.configly.structure.environment.domain.exception;

import com.configly.model.environment.EnvironmentId;
import com.configly.model.project.ProjectId;

public class EnvironmentNotAssignedToProjectException extends RuntimeException {
    public EnvironmentNotAssignedToProjectException(EnvironmentId environmentId, ProjectId projectId) {
        super("Environment: [" + environmentId.idAsString() + "] is not assigned to project: [" + projectId.idAsString() + "]");
    }
}
