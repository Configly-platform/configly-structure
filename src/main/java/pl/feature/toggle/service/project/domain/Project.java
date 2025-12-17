package pl.feature.toggle.service.project.domain;

import com.ftaas.domain.project.ProjectDescription;
import com.ftaas.domain.project.ProjectId;
import com.ftaas.domain.project.ProjectName;
import pl.feature.toggle.service.project.application.port.in.CreateProjectCommand;

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
