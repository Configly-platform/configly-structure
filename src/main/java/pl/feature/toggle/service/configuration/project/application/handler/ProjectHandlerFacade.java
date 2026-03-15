package pl.feature.toggle.service.configuration.project.application.handler;

import pl.feature.toggle.service.configuration.project.application.policy.ProjectPolicyFacade;
import pl.feature.toggle.service.configuration.project.application.port.in.ChangeProjectStatusUseCase;
import pl.feature.toggle.service.configuration.project.application.port.in.CreateProjectUseCase;
import pl.feature.toggle.service.configuration.project.application.port.in.UpdateProjectUseCase;
import pl.feature.toggle.service.configuration.project.application.port.out.environment.EnvironmentStatusCascadePort;
import pl.feature.toggle.service.configuration.project.application.port.out.ProjectCommandRepository;
import pl.feature.toggle.service.configuration.project.application.port.out.ProjectQueryRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import pl.feature.toggle.service.model.security.actor.ActorProvider;
import pl.feature.toggle.service.model.security.correlation.CorrelationProvider;
import pl.feature.toggle.service.outbox.api.OutboxWriter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProjectHandlerFacade {

    public static CreateProjectUseCase createProjectUseCase(
            ProjectCommandRepository projectCommandRepository,
            ProjectPolicyFacade projectPolicyFacade,
            OutboxWriter outboxWriter
    ) {
        return new CreateProjectHandler(projectCommandRepository, projectPolicyFacade, outboxWriter);
    }

    public static UpdateProjectUseCase updateProjectUseCase(
            ProjectCommandRepository projectCommandRepository,
            ProjectQueryRepository projectQueryRepository,
            ProjectPolicyFacade projectPolicyFacade,
            OutboxWriter outboxWriter
    ) {
        return new UpdateProjectHandler(projectCommandRepository, projectQueryRepository, projectPolicyFacade, outboxWriter);
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
