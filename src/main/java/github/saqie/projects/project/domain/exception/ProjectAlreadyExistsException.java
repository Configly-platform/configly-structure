package github.saqie.projects.project.domain.exception;

import com.ftaas.domain.project.ProjectName;

public class ProjectAlreadyExistsException extends RuntimeException {
    public ProjectAlreadyExistsException(ProjectName projectName) {
        super(String.format("Project with name '%s' already exists", projectName.value()));
    }
}
