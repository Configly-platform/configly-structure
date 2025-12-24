package pl.feature.toggle.service.configuration.project.application.handler;

import pl.feature.toggle.service.configuration.project.domain.Project;
import pl.feature.toggle.service.contracts.event.projects.ProjectCreated;
import pl.feature.toggle.service.contracts.shared.Metadata;
import pl.feature.toggle.service.model.security.Actor;

import static pl.feature.toggle.service.contracts.event.projects.ProjectCreated.projectCreatedEventBuilder;

final class ProjectHandlerEventMapper {

    static ProjectCreated createProjectCreatedEvent(Project project, Actor actor) {
        return projectCreatedEventBuilder()
                .projectName(project.name().value())
                .projectId(project.id().uuid())
                .metadata(Metadata.create(actor.actorId().value(), actor.username().value()))
                .build();
    }

}
