package pl.feature.toggle.service.configuration.project.application.handler;

import pl.feature.toggle.service.configuration.project.application.port.in.ChangeProjectStatusUseCase;
import pl.feature.toggle.service.configuration.project.application.port.in.CreateProjectUseCase;
import pl.feature.toggle.service.configuration.project.application.port.in.UpdateProjectUseCase;
import pl.feature.toggle.service.configuration.project.application.port.out.EnvironmentStatusCascadePort;
import pl.feature.toggle.service.configuration.project.application.port.out.ProjectRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import pl.feature.toggle.service.model.security.actor.ActorProvider;
import pl.feature.toggle.service.model.security.correlation.CorrelationProvider;
import pl.feature.toggle.service.outbox.api.OutboxWriter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProjectHandlerFacade {

    public static CreateProjectUseCase createProjectUseCase(
            ProjectRepository projectRepository,
            OutboxWriter outboxWriter,
            ActorProvider actorProvider,
            CorrelationProvider correlationProvider
    ) {
        return new CreateProjectHandler(projectRepository, outboxWriter, actorProvider, correlationProvider);
    }

    public static UpdateProjectUseCase updateProjectUseCase(
            ProjectRepository projectRepository,
            OutboxWriter outboxWriter,
            ActorProvider actorProvider,
            CorrelationProvider correlationProvider
    ) {
        return new UpdateProjectHandler(projectRepository, actorProvider, correlationProvider, outboxWriter);
    }

    public static ChangeProjectStatusUseCase changeProjectStatusUseCase(
            ProjectRepository projectRepository,
            OutboxWriter outboxWriter,
            ActorProvider actorProvider,
            CorrelationProvider correlationProvider,
            EnvironmentStatusCascadePort environmentStatusCascadePort
    ) {
        return new ChangeProjectStatusHandler(projectRepository, actorProvider,
                correlationProvider, outboxWriter, environmentStatusCascadePort);
    }

}
