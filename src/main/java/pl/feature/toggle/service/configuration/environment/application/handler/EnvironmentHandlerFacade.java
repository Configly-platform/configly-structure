package pl.feature.toggle.service.configuration.environment.application.handler;

import pl.feature.toggle.service.configuration.environment.application.port.in.CreateEnvironmentUseCase;
import pl.feature.toggle.service.configuration.environment.application.port.out.EnvironmentRepository;
import pl.feature.toggle.service.configuration.project.application.port.out.ProjectRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import pl.feature.toggle.service.model.security.ActorProvider;
import pl.feature.toggle.service.outbox.api.OutboxWriter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class EnvironmentHandlerFacade {

    public static CreateEnvironmentUseCase createEnvironmentUseCase(
            EnvironmentRepository environmentRepository,
            ProjectRepository projectRepository,
            OutboxWriter outboxWriter,
            ActorProvider actorProvider
    ) {
        return new CreateEnvironmentHandler(environmentRepository, projectRepository, outboxWriter, actorProvider);
    }

}
