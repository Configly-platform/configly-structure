package github.saqie.projects.project.application.handler;

import com.ftaas.contracts.event.projects.ProjectCreated;
import github.saqie.projects.project.domain.Project;

import static com.ftaas.contracts.event.projects.ProjectCreated.projectCreatedEventBuilder;

final class ProjectHandlerEventMapper {

    static ProjectCreated createProjectCreatedEvent(Project project) {
        return projectCreatedEventBuilder()
                .projectName(project.name().value())
                .projectId(project.id().uuid())
                .build();
    }

}
