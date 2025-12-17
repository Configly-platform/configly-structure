package github.saqie.projects.environment.domain.exception;

import github.saqie.projects.environment.domain.Environment;

public class EnvironmentAlreadyExistsException extends RuntimeException {
    public EnvironmentAlreadyExistsException(final Environment environment) {
        super(String.format("Environment with name '%s' already exists for project '%s' ",
                environment.name().value(), environment.projectId().toString()));
    }
}
