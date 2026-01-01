package pl.feature.toggle.service.configuration.project.application.handler;

import pl.feature.toggle.service.configuration.project.domain.Project;
import pl.feature.toggle.service.contracts.event.projects.ProjectCreated;
import pl.feature.toggle.service.contracts.shared.Metadata;
import pl.feature.toggle.service.model.security.actor.Actor;
import pl.feature.toggle.service.model.security.correlation.CorrelationId;

import static pl.feature.toggle.service.contracts.event.projects.ProjectCreated.projectCreatedEventBuilder;

final class ProjectHandlerEventMapper {

    static ProjectCreated createProjectCreatedEvent(Project project, Actor actor, CorrelationId correlationId) {
        return projectCreatedEventBuilder()
                .projectName(project.name().value())
                .projectId(project.id().uuid())
                .metadata(Metadata.create(actor.idAsString(), actor.usernameAsString(), correlationId.value()))
                .build();
    }

}
