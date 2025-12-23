package pl.feature.toggle.service.configuration.environment.application.handler;

import pl.feature.toggle.service.configuration.environment.domain.Environment;
import pl.feature.toggle.service.contracts.event.projects.EnvironmentCreated;

import static pl.feature.toggle.service.contracts.event.projects.EnvironmentCreated.environmentCreatedEventBuilder;


final class EnvironmentHandlerEventMapper {

    static EnvironmentCreated createEnvironmentCreatedEvent(Environment environment) {
        return environmentCreatedEventBuilder()
                .environmentId(environment.id().uuid())
                .projectId(environment.projectId().uuid())
                .environmentName(environment.name().value())
                .build();
    }

}
