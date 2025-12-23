package pl.feature.toggle.service.configuration.project.domain.exception;


import pl.feature.toggle.service.model.project.ProjectName;

public class ProjectAlreadyExistsException extends RuntimeException {
    public ProjectAlreadyExistsException(ProjectName projectName) {
        super(String.format("Project with name '%s' already exists", projectName.value()));
    }
}
