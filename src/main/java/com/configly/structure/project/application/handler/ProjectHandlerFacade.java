package com.configly.structure.project.application.handler;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import com.configly.structure.project.application.port.in.ChangeProjectStatusUseCase;
import com.configly.structure.project.application.port.in.CreateProjectUseCase;
import com.configly.structure.project.application.port.in.UpdateProjectUseCase;
import com.configly.structure.project.application.port.out.ProjectCommandRepository;
import com.configly.structure.project.application.port.out.ProjectQueryRepository;
import com.configly.structure.project.application.port.out.environment.EnvironmentStatusCascadePort;
import com.configly.outbox.api.OutboxWriter;

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
