package pl.feature.toggle.service.configuration.environment.application.handler;

import pl.feature.toggle.service.configuration.environment.domain.Environment;
import pl.feature.toggle.service.configuration.environment.domain.EnvironmentField;
import pl.feature.toggle.service.configuration.environment.domain.EnvironmentType;
import pl.feature.toggle.service.configuration.environment.domain.EnvironmentUpdateResult;
import pl.feature.toggle.service.configuration.project.domain.ProjectField;
import pl.feature.toggle.service.configuration.project.domain.ProjectStatus;
import pl.feature.toggle.service.configuration.project.domain.ProjectUpdateResult;
import pl.feature.toggle.service.contracts.event.environment.EnvironmentCreated;
import pl.feature.toggle.service.contracts.event.environment.EnvironmentStatusChanged;
import pl.feature.toggle.service.contracts.event.environment.EnvironmentTypeChanged;
import pl.feature.toggle.service.contracts.event.environment.EnvironmentUpdated;
import pl.feature.toggle.service.contracts.shared.Changes;
import pl.feature.toggle.service.contracts.shared.Metadata;
import pl.feature.toggle.service.model.project.ProjectDescription;
import pl.feature.toggle.service.model.project.ProjectId;
import pl.feature.toggle.service.model.project.ProjectName;
import pl.feature.toggle.service.model.security.actor.Actor;
import pl.feature.toggle.service.model.security.correlation.CorrelationId;

import static pl.feature.toggle.service.contracts.event.environment.EnvironmentCreated.environmentCreatedEventBuilder;
import static pl.feature.toggle.service.contracts.event.environment.EnvironmentStatusChanged.environmentStatusChangedBuilder;
import static pl.feature.toggle.service.contracts.event.environment.EnvironmentTypeChanged.environmentTypeChangedBuilder;
import static pl.feature.toggle.service.contracts.event.environment.EnvironmentUpdated.environmentUpdatedEventBuilder;


final class EventMapper {

    static EnvironmentCreated createEnvironmentCreatedEvent(Environment environment, Actor actor, CorrelationId correlationId) {
        return environmentCreatedEventBuilder()
                .environmentId(environment.id().uuid())
                .projectId(environment.projectId().uuid())
                .environmentName(environment.name().value())
                .metadata(Metadata.create(actor.idAsString(), actor.usernameAsString(), correlationId.value()))
                .build();
    }

    static EnvironmentStatusChanged createEnvironmentStatusChangedEvent(EnvironmentUpdateResult updateResult, Actor actor, CorrelationId correlationId) {
        return environmentStatusChangedBuilder()
                .environmentId(updateResult.environment().id().uuid())
                .projectId(updateResult.environment().projectId().uuid())
                .status(updateResult.environment().status().name())
                .changes(buildChanges(updateResult))
                .metadata(Metadata.create(actor.idAsString(), actor.usernameAsString(), correlationId.value()))
                .build();
    }

    static EnvironmentTypeChanged createEnvironmentTypeChangedEvent(EnvironmentUpdateResult updateResult, Actor actor, CorrelationId correlationId) {
        return environmentTypeChangedBuilder()
                .environmentId(updateResult.environment().id().uuid())
                .projectId(updateResult.environment().projectId().uuid())
                .type(updateResult.environment().type().name())
                .changes(buildChanges(updateResult))
                .metadata(Metadata.create(actor.idAsString(), actor.usernameAsString(), correlationId.value()))
                .build();
    }

    static EnvironmentUpdated createEnvironmentUpdatedEvent(EnvironmentUpdateResult updateResult, Actor actor, CorrelationId correlationId) {
        return environmentUpdatedEventBuilder()
                .environmentId(updateResult.environment().id().uuid())
                .projectId(updateResult.environment().projectId().uuid())
                .metadata(Metadata.create(actor.idAsString(), actor.usernameAsString(), correlationId.value()))
                .environmentName(updateResult.environment().name().value())
                .build();
    }

    private static Changes buildChanges(EnvironmentUpdateResult updateResult) {
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

    private static String serialize(EnvironmentField field, Object value) {
        if (value == null) {
            return null;
        }

        return switch (field) {
            case NAME -> ((ProjectName) value).value();
            case PROJECT_ID -> ((ProjectId) value).idAsString();
            case STATUS -> ((ProjectStatus) value).name();
            case TYPE -> ((EnvironmentType) value).name();
        };
    }

}
