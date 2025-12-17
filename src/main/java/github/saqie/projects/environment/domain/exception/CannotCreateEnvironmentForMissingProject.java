package github.saqie.projects.environment.domain.exception;

import com.ftaas.domain.project.ProjectId;

public class CannotCreateEnvironmentForMissingProject extends RuntimeException {

    public CannotCreateEnvironmentForMissingProject(ProjectId projectId) {
        super("Cannot create environment for missing project: " + projectId.toString());
    }
}
