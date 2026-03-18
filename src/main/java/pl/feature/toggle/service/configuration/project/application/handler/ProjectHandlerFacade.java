package pl.feature.toggle.service.configuration.project.application.handler;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import pl.feature.toggle.service.configuration.project.application.port.in.ChangeProjectStatusUseCase;
import pl.feature.toggle.service.configuration.project.application.port.in.CreateProjectUseCase;
import pl.feature.toggle.service.configuration.project.application.port.in.UpdateProjectUseCase;
import pl.feature.toggle.service.configuration.project.application.port.out.ProjectCommandRepository;
import pl.feature.toggle.service.configuration.project.application.port.out.ProjectQueryRepository;
import pl.feature.toggle.service.configuration.project.application.port.out.environment.EnvironmentStatusCascadePort;
import pl.feature.toggle.service.outbox.api.OutboxWriter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProjectHandlerFacade {

    public static CreateProjectUseCase createProjectUseCase(
            ProjectCommandRepository projectCommandRepository,
            OutboxWriter outboxWriter
    ) {
        return new CreateProjectHandler(projectCommandRepository, outboxWriter);
    }

    public static UpdateProjectUseCase updateProjectUseCase(
            ProjectCommandRepository projectCommandRepository,
            ProjectQueryRepository projectQueryRepository,
            OutboxWriter outboxWriter
    ) {
        return new UpdateProjectHandler(projectCommandRepository, projectQueryRepository, outboxWriter);
    }

    public static ChangeProjectStatusUseCase changeProjectStatusUseCase(
            ProjectCommandRepository projectCommandRepository,
            ProjectQueryRepository projectQueryRepository,
            OutboxWriter outboxWriter,
            EnvironmentStatusCascadePort environmentStatusCascadePort
    ) {
        return new ChangeProjectStatusHandler(projectCommandRepository, projectQueryRepository, outboxWriter, environmentStatusCascadePort);
    }

}
