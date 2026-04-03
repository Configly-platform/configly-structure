package com.configly.structure.project.application.handler;

import com.configly.structure.environment.domain.EnvironmentField;
import com.configly.structure.project.application.port.out.environment.CascadedEnvironmentStatusChange;
import com.configly.structure.project.domain.Project;
import com.configly.structure.project.domain.ProjectField;
import com.configly.structure.project.domain.ProjectUpdateResult;
import com.configly.contracts.event.environment.EnvironmentStatusChanged;
import com.configly.contracts.event.project.ProjectCreated;
import com.configly.contracts.event.project.ProjectStatusChanged;
import com.configly.contracts.event.project.ProjectUpdated;
import com.configly.contracts.shared.Changes;
import com.configly.contracts.shared.Metadata;
import com.configly.model.project.ProjectDescription;
import com.configly.model.project.ProjectName;
import com.configly.model.project.ProjectStatus;
import com.configly.web.actor.Actor;
import com.configly.web.correlation.CorrelationId;

import java.time.LocalDateTime;

import static com.configly.contracts.event.environment.EnvironmentStatusChanged.environmentStatusChangedEventBuilder;
import static com.configly.contracts.event.project.ProjectCreated.projectCreatedEventBuilder;
import static com.configly.contracts.event.project.ProjectStatusChanged.projectStatusChangedEventBuilder;
import static com.configly.contracts.event.project.ProjectUpdated.projectUpdatedEventBuilder;

final class EventMapper {

    static ProjectCreated createProjectCreatedEvent(Project project, Actor actor, CorrelationId correlationId) {
        return projectCreatedEventBuilder()
                .projectName(project.name().value())
                .projectId(project.id().uuid())
                .status(project.status().name())
                .createdAt(project.createdAt().toLocalDateTime())
                .updatedAt(project.updatedAt().toLocalDateTime())
                .revision(project.revision().value())
                .projectDescription(project.description().value())
                .metadata(Metadata.create(actor.idAsString(), actor.usernameAsString(), correlationId.value()))
                .build();
    }

    static ProjectStatusChanged createProjectStatusChangedEvent(ProjectUpdateResult updateResult, Actor actor, CorrelationId correlationId) {
        return projectStatusChangedEventBuilder()
                .projectId(updateResult.project().id().uuid())
                .metadata(Metadata.create(actor.idAsString(), actor.usernameAsString(), correlationId.value()))
                .status(updateResult.project().status().name())
                .createdAt(updateResult.project().createdAt().toLocalDateTime())
                .updatedAt(updateResult.project().updatedAt().toLocalDateTime())
                .revision(updateResult.project().revision().value())
                .changes(buildChanges(updateResult))
                .build();
    }

    static ProjectUpdated createProjectUpdatedEvent(ProjectUpdateResult updateResult, Actor actor, CorrelationId correlationId) {
        return projectUpdatedEventBuilder()
                .projectId(updateResult.project().id().uuid())
                .metadata(Metadata.create(actor.idAsString(), actor.usernameAsString(), correlationId.value()))
                .changes(buildChanges(updateResult))
                .projectDescription(updateResult.project().description().value())
                .createdAt(updateResult.project().createdAt().toLocalDateTime())
                .updatedAt(updateResult.project().updatedAt().toLocalDateTime())
                .revision(updateResult.project().revision().value())
                .status(updateResult.project().status().name())
                .projectName(updateResult.project().name().value())
                .build();
    }

    static EnvironmentStatusChanged createEnvironmentStatusChanged(CascadedEnvironmentStatusChange statusChange, Actor actor, CorrelationId correlationId) {
        return environmentStatusChangedEventBuilder()
                .environmentId(statusChange.environmentId().uuid())
                .projectId(statusChange.projectId().uuid())
                .status(statusChange.afterStatus().name())
                .createdAt(statusChange.createdAt().toLocalDateTime())
                .updatedAt(LocalDateTime.now())
                .changes(Changes.of(EnvironmentField.STATUS.name(), statusChange.beforeStatus().name(), statusChange.afterStatus().name()))
                .revision(statusChange.revision().value())
                .metadata(Metadata.create(actor.idAsString(), actor.usernameAsString(), correlationId.value()))
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
