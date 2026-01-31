package pl.feature.toggle.service.configuration.project.application.handler;

import pl.feature.toggle.service.configuration.project.domain.Project;
import pl.feature.toggle.service.configuration.project.domain.ProjectField;
import pl.feature.toggle.service.configuration.project.domain.ProjectUpdateResult;
import pl.feature.toggle.service.configuration.project.domain.ProjectStatus;
import pl.feature.toggle.service.contracts.event.project.ProjectCreated;
import pl.feature.toggle.service.contracts.event.project.ProjectStatusChanged;
import pl.feature.toggle.service.contracts.event.project.ProjectUpdated;
import pl.feature.toggle.service.contracts.shared.Changes;
import pl.feature.toggle.service.contracts.shared.Metadata;
import pl.feature.toggle.service.model.project.ProjectDescription;
import pl.feature.toggle.service.model.project.ProjectName;
import pl.feature.toggle.service.model.security.actor.Actor;
import pl.feature.toggle.service.model.security.correlation.CorrelationId;

import static pl.feature.toggle.service.contracts.event.project.ProjectCreated.projectCreatedEventBuilder;
import static pl.feature.toggle.service.contracts.event.project.ProjectStatusChanged.projectStatusChangedEventBuilder;
import static pl.feature.toggle.service.contracts.event.project.ProjectUpdated.projectUpdatedEventBuilder;

final class EventMapper {

    static ProjectCreated createProjectCreatedEvent(Project project, Actor actor, CorrelationId correlationId) {
        return projectCreatedEventBuilder()
                .projectName(project.name().value())
                .projectId(project.id().uuid())
                .metadata(Metadata.create(actor.idAsString(), actor.usernameAsString(), correlationId.value()))
                .build();
    }

    static ProjectStatusChanged createProjectStatusChangedEvent(ProjectUpdateResult updateResult, Actor actor, CorrelationId correlationId) {
        return projectStatusChangedEventBuilder()
                .projectId(updateResult.project().id().uuid())
                .metadata(Metadata.create(actor.idAsString(), actor.usernameAsString(), correlationId.value()))
                .status(updateResult.project().status().name())
                .changes(buildChanges(updateResult))
                .build();
    }

    static ProjectUpdated createProjectUpdatedEvent(ProjectUpdateResult updateResult, Actor actor, CorrelationId correlationId) {
        return projectUpdatedEventBuilder()
                .projectId(updateResult.project().id().uuid())
                .metadata(Metadata.create(actor.idAsString(), actor.usernameAsString(), correlationId.value()))
                .changes(buildChanges(updateResult))
                .status(updateResult.project().status().name())
                .projectName(updateResult.project().name().value())
                .build();
    }

    private static Changes buildChanges(ProjectUpdateResult updateResult) {
        return new Changes(
                updateResult.changes().stream()
                        .map(it -> Changes.buildChange(
                                it.field().name(),
                                serialize(it.field(), it.before()),
                                serialize(it.field(), it.after())
                        ))
                        .toList()
        );
    }

    private static String serialize(ProjectField field, Object value) {
        if (value == null) {
            return null;
        }

        return switch (field) {
            case NAME -> ((ProjectName) value).value();
            case DESCRIPTION -> ((ProjectDescription) value).value();
            case STATUS -> ((ProjectStatus) value).name();
        };
    }

}
