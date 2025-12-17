package pl.feature.toggle.service.project.application.handler;

import github.saqie.ftaasoutbox.api.OutboxWriter;
import pl.feature.toggle.service.project.application.port.in.CreateProjectUseCase;
import pl.feature.toggle.service.project.application.port.out.ProjectRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProjectHandlerFacade {

    public static CreateProjectUseCase  createProjectUseCase(
            ProjectRepository projectRepository,
            OutboxWriter outboxWriter
    ) {
        return new CreateProjectHandler(projectRepository, outboxWriter);
    }

}
