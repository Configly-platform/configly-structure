package pl.feature.toggle.service.configuration.environment.application.handler;

import pl.feature.toggle.service.configuration.environment.domain.Environment;
import pl.feature.toggle.service.contracts.event.projects.EnvironmentCreated;
import pl.feature.toggle.service.contracts.shared.Metadata;
import pl.feature.toggle.service.model.security.actor.Actor;
import pl.feature.toggle.service.model.security.correlation.CorrelationId;

import static pl.feature.toggle.service.contracts.event.projects.EnvironmentCreated.environmentCreatedEventBuilder;


final class EnvironmentHandlerEventMapper {

    static EnvironmentCreated createEnvironmentCreatedEvent(Environment environment, Actor actor, CorrelationId correlationId) {
        return environmentCreatedEventBuilder()
                .environmentId(environment.id().uuid())
                .projectId(environment.projectId().uuid())
                .environmentName(environment.name().value())
                .metadata(Metadata.create(actor.idAsString(), actor.usernameAsString(), correlationId.value()))
                .build();
    }

}
