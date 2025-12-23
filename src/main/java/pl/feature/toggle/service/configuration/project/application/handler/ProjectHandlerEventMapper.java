package pl.feature.toggle.service.configuration.project.application.handler;

import pl.feature.toggle.service.configuration.project.domain.Project;
import pl.feature.toggle.service.contracts.event.projects.ProjectCreated;

import static pl.feature.toggle.service.contracts.event.projects.ProjectCreated.projectCreatedEventBuilder;

final class ProjectHandlerEventMapper {

    static ProjectCreated createProjectCreatedEvent(Project project) {
        return projectCreatedEventBuilder()
                .projectName(project.name().value())
                .projectId(project.id().uuid())
                .build();
    }

}
