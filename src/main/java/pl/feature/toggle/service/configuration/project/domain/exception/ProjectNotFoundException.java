package pl.feature.toggle.service.configuration.project.domain.exception;

import com.ftaas.domain.project.ProjectId;

public class ProjectNotFoundException extends RuntimeException {
    public ProjectNotFoundException(ProjectId projectId) {
        super(String.format("Project with id '%s' not found", projectId.toString()));
    }
}
