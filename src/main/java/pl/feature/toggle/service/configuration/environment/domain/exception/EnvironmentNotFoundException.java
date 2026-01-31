package pl.feature.toggle.service.configuration.environment.domain.exception;


import pl.feature.toggle.service.model.environment.EnvironmentId;
import pl.feature.toggle.service.model.project.ProjectId;

public class EnvironmentNotFoundException extends RuntimeException {
    public EnvironmentNotFoundException(EnvironmentId environmentId, ProjectId projectId) {
        super(String.format("Environment with id '%s' not found for project '%s'", environmentId.idAsString(), projectId.idAsString()));
    }
}
