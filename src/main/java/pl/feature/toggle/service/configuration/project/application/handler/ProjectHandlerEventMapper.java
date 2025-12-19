package pl.feature.toggle.service.configuration.project.application.handler;

import com.ftaas.contracts.event.projects.ProjectCreated;
import pl.feature.toggle.service.configuration.project.domain.Project;

import static com.ftaas.contracts.event.projects.ProjectCreated.projectCreatedEventBuilder;

final class ProjectHandlerEventMapper {

    static ProjectCreated createProjectCreatedEvent(Project project) {
        return projectCreatedEventBuilder()
                .projectName(project.name().value())
                .projectId(project.id().uuid())
                .build();
    }

}
