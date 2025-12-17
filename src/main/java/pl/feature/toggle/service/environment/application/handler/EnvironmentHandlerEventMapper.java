package pl.feature.toggle.service.environment.application.handler;

import com.ftaas.contracts.event.projects.EnvironmentCreated;
import pl.feature.toggle.service.environment.domain.Environment;

import static com.ftaas.contracts.event.projects.EnvironmentCreated.environmentCreatedEventBuilder;

final class EnvironmentHandlerEventMapper {

    static EnvironmentCreated createEnvironmentCreatedEvent(Environment environment) {
        return environmentCreatedEventBuilder()
                .environmentId(environment.id().uuid())
                .projectId(environment.projectId().uuid())
                .environmentName(environment.name().value())
                .build();
    }

}
