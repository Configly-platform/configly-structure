package pl.feature.toggle.service.configuration.environment.domain.exception;


import pl.feature.toggle.service.model.project.ProjectId;

public class CannotCreateEnvironmentForMissingProject extends RuntimeException {

    public CannotCreateEnvironmentForMissingProject(ProjectId projectId) {
        super("Cannot create environment for missing project: " + projectId.toString());
    }
}
