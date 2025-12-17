package github.saqie.projects.environment.application.handler;

import github.saqie.ftaasoutbox.api.OutboxWriter;
import github.saqie.projects.environment.application.port.in.CreateEnvironmentUseCase;
import github.saqie.projects.environment.application.port.out.EnvironmentRepository;
import github.saqie.projects.project.application.port.out.ProjectRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class EnvironmentHandlerFacade {

    public static CreateEnvironmentUseCase createEnvironmentUseCase(
            EnvironmentRepository environmentRepository,
            ProjectRepository projectRepository,
            OutboxWriter outboxWriter
    ) {
        return new CreateEnvironmentHandler(environmentRepository, projectRepository, outboxWriter);
    }

}
