package pl.feature.toggle.service.configuration.project.domain;

import pl.feature.toggle.service.configuration.project.application.port.in.CreateProjectCommand;
import pl.feature.toggle.service.model.project.ProjectDescription;
import pl.feature.toggle.service.model.project.ProjectId;
import pl.feature.toggle.service.model.project.ProjectName;

public record Project(
        ProjectId id,
        ProjectName name,
        ProjectDescription description
) {

    public static Project create(CreateProjectCommand command) {
        ProjectId projectId = ProjectId.create();
        return new Project(projectId, command.name(), command.description());
    }

    public static Project create(ProjectName name, ProjectDescription description) {
        ProjectId projectId = ProjectId.create();
        return new Project(projectId, name, description);
    }

    public static Project create(ProjectId projectId, ProjectName name, ProjectDescription description) {
        return new Project(projectId, name, description);
    }

}
