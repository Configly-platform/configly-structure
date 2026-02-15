package pl.feature.toggle.service.configuration.environment.application.handler;

import pl.feature.toggle.service.configuration.environment.domain.*;
import pl.feature.toggle.service.contracts.event.environment.EnvironmentCreated;
import pl.feature.toggle.service.contracts.event.environment.EnvironmentStatusChanged;
import pl.feature.toggle.service.contracts.event.environment.EnvironmentTypeChanged;
import pl.feature.toggle.service.contracts.event.environment.EnvironmentUpdated;
import pl.feature.toggle.service.contracts.shared.Changes;
import pl.feature.toggle.service.contracts.shared.Metadata;
import pl.feature.toggle.service.model.environment.EnvironmentName;
import pl.feature.toggle.service.model.environment.EnvironmentStatus;
import pl.feature.toggle.service.model.project.ProjectId;
import pl.feature.toggle.service.model.security.actor.Actor;
import pl.feature.toggle.service.model.security.correlation.CorrelationId;

import static pl.feature.toggle.service.contracts.event.environment.EnvironmentCreated.environmentCreatedEventBuilder;
import static pl.feature.toggle.service.contracts.event.environment.EnvironmentStatusChanged.environmentStatusChangedEventBuilder;
import static pl.feature.toggle.service.contracts.event.environment.EnvironmentTypeChanged.environmentTypeChangedBuilder;
import static pl.feature.toggle.service.contracts.event.environment.EnvironmentUpdated.environmentUpdatedEventBuilder;


final class EventMapper {

    static EnvironmentCreated createEnvironmentCreatedEvent(Environment environment, Actor actor, CorrelationId correlationId) {
        return environmentCreatedEventBuilder()
                .environmentId(environment.id().uuid())
                .projectId(environment.projectId().uuid())
                .environmentName(environment.name().value())
                .revision(environment.revision().value())
                .createdAt(environment.createdAt().toLocalDateTime())
                .updatedAt(environment.updatedAt().toLocalDateTime())
                .metadata(Metadata.create(actor.idAsString(), actor.usernameAsString(), correlationId.value()))
                .build();
    }

    static EnvironmentStatusChanged createEnvironmentStatusChangedEvent(EnvironmentUpdateResult updateResult, Actor actor, CorrelationId correlationId) {
        return environmentStatusChangedEventBuilder()
                .environmentId(updateResult.environment().id().uuid())
                .projectId(updateResult.environment().projectId().uuid())
                .status(updateResult.environment().status().name())
                .createdAt(updateResult.environment().createdAt().toLocalDateTime())
                .updatedAt(updateResult.environment().updatedAt().toLocalDateTime())
                .changes(buildChanges(updateResult))
                .revision(updateResult.environment().revision().value())
                .metadata(Metadata.create(actor.idAsString(), actor.usernameAsString(), correlationId.value()))
                .build();
    }

    static EnvironmentTypeChanged createEnvironmentTypeChangedEvent(EnvironmentUpdateResult updateResult, Actor actor, CorrelationId correlationId) {
        return environmentTypeChangedBuilder()
                .environmentId(updateResult.environment().id().uuid())
                .projectId(updateResult.environment().projectId().uuid())
                .createdAt(updateResult.environment().createdAt().toLocalDateTime())
                .updatedAt(updateResult.environment().updatedAt().toLocalDateTime())
                .type(updateResult.environment().type().name())
                .changes(buildChanges(updateResult))
                .revision(updateResult.environment().revision().value())
                .metadata(Metadata.create(actor.idAsString(), actor.usernameAsString(), correlationId.value()))
                .build();
    }

    static EnvironmentUpdated createEnvironmentUpdatedEvent(EnvironmentUpdateResult updateResult, Actor actor, CorrelationId correlationId) {
        return environmentUpdatedEventBuilder()
                .changes(buildChanges(updateResult))
                .revision(updateResult.environment().revision().value())
                .createdAt(updateResult.environment().createdAt().toLocalDateTime())
                .updatedAt(updateResult.environment().updatedAt().toLocalDateTime())
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
            case NAME -> ((EnvironmentName) value).value();
            case PROJECT_ID -> ((ProjectId) value).idAsString();
            case STATUS -> ((EnvironmentStatus) value).name();
            case TYPE -> ((EnvironmentType) value).name();
        };
    }

}
