package pl.feature.toggle.service.configuration.project.infrastructure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.feature.toggle.service.configuration.project.application.handler.ProjectHandlerFacade;
import pl.feature.toggle.service.configuration.project.application.policy.ProjectPolicyFacade;
import pl.feature.toggle.service.configuration.project.application.port.in.ChangeProjectStatusUseCase;
import pl.feature.toggle.service.configuration.project.application.port.in.CreateProjectUseCase;
import pl.feature.toggle.service.configuration.project.application.port.in.UpdateProjectUseCase;
import pl.feature.toggle.service.configuration.project.application.port.out.ProjectCommandRepository;
import pl.feature.toggle.service.configuration.project.application.port.out.ProjectQueryRepository;
import pl.feature.toggle.service.configuration.project.application.port.out.environment.EnvironmentStatusCascadePort;
import pl.feature.toggle.service.outbox.api.OutboxWriter;

@Configuration("projectApplicationConfig")
class Config {

    @Bean
    CreateProjectUseCase createProjectUseCase(
            ProjectCommandRepository projectCommandRepository,
            ProjectPolicyFacade projectPolicyFacade,
            OutboxWriter outboxWriter
    ) {
        return ProjectHandlerFacade.createProjectUseCase(projectCommandRepository, projectPolicyFacade,
                outboxWriter);
    }

    @Bean
    UpdateProjectUseCase updateProjectUseCase(
            ProjectCommandRepository projectCommandRepository,
            ProjectQueryRepository projectQueryRepository,
            ProjectPolicyFacade projectPolicyFacade,
            OutboxWriter outboxWriter
    ) {
        return ProjectHandlerFacade.updateProjectUseCase(projectCommandRepository, projectQueryRepository,
                projectPolicyFacade, outboxWriter);
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
