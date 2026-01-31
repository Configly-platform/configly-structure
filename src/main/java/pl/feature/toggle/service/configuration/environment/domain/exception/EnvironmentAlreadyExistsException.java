package pl.feature.toggle.service.configuration.environment.domain.exception;

import pl.feature.toggle.service.model.environment.EnvironmentName;
import pl.feature.toggle.service.model.project.ProjectId;

public class EnvironmentAlreadyExistsException extends RuntimeException {
    public EnvironmentAlreadyExistsException(EnvironmentName environmentName, ProjectId projectId) {
        super(String.format("Environment with name '%s' already exists for project '%s' ",
                environmentName.value(), projectId.idAsString()));
    }
}
