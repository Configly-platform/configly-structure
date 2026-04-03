package com.configly.structure.environment.domain.exception;

import com.configly.model.environment.EnvironmentName;
import com.configly.model.project.ProjectId;

public class EnvironmentAlreadyExistsException extends RuntimeException {
    public EnvironmentAlreadyExistsException(EnvironmentName environmentName, ProjectId projectId) {
        super(String.format("Environment with name '%s' already exists for project '%s' ",
                environmentName.value(), projectId.idAsString()));
    }
}
