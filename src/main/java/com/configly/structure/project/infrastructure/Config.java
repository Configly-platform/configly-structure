package com.configly.structure.project.infrastructure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.configly.structure.project.application.handler.ProjectHandlerFacade;
import com.configly.structure.project.application.port.in.ChangeProjectStatusUseCase;
import com.configly.structure.project.application.port.in.CreateProjectUseCase;
import com.configly.structure.project.application.port.in.UpdateProjectUseCase;
import com.configly.structure.project.application.port.out.ProjectCommandRepository;
import com.configly.structure.project.application.port.out.ProjectQueryRepository;
import com.configly.structure.project.application.port.out.environment.EnvironmentStatusCascadePort;
import com.configly.outbox.api.OutboxWriter;

@Configuration("projectApplicationConfig")
class Config {

    @Bean
    CreateProjectUseCase createProjectUseCase(
            ProjectCommandRepository projectCommandRepository,
            OutboxWriter outboxWriter
    ) {
        return ProjectHandlerFacade.createProjectUseCase(projectCommandRepository, outboxWriter);
    }

    @Bean
    UpdateProjectUseCase updateProjectUseCase(
            ProjectCommandRepository projectCommandRepository,
            ProjectQueryRepository projectQueryRepository,
            OutboxWriter outboxWriter
    ) {
        return ProjectHandlerFacade.updateProjectUseCase(projectCommandRepository, projectQueryRepository,
                outboxWriter);
    }

    @Bean
    ChangeProjectStatusUseCase changeProjectStatusUseCase(
            ProjectCommandRepository projectCommandRepository,
            ProjectQueryRepository projectQueryRepository,
            OutboxWriter outboxWriter,
            EnvironmentStatusCascadePort environmentStatusCascadePort
    ) {
        return ProjectHandlerFacade.changeProjectStatusUseCase(projectCommandRepository, projectQueryRepository, outboxWriter,
                environmentStatusCascadePort);
    }

}
