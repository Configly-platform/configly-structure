package com.configly.structure.environment.domain.exception;


import com.configly.model.environment.EnvironmentId;
import com.configly.model.project.ProjectId;

public class EnvironmentNotFoundException extends RuntimeException {
    public EnvironmentNotFoundException(EnvironmentId environmentId, ProjectId projectId) {
        super(String.format("Environment with id '%s' not found for project '%s'", environmentId.idAsString(), projectId.idAsString()));
    }
}
