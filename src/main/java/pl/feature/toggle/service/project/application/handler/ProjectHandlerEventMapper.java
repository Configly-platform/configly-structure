package pl.feature.toggle.service.project.application.handler;

import com.ftaas.contracts.event.projects.ProjectCreated;
import pl.feature.toggle.service.project.domain.Project;

import static com.ftaas.contracts.event.projects.ProjectCreated.projectCreatedEventBuilder;

final class ProjectHandlerEventMapper {

    static ProjectCreated createProjectCreatedEvent(Project project) {
        return projectCreatedEventBuilder()
                .projectName(project.name().value())
                .projectId(project.id().uuid())
                .build();
    }

}
