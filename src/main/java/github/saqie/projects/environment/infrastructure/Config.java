package github.saqie.projects.environment.infrastructure;

import github.saqie.ftaasoutbox.api.OutboxWriter;
import github.saqie.projects.environment.application.handler.EnvironmentHandlerFacade;
import github.saqie.projects.environment.application.port.in.CreateEnvironmentUseCase;
import github.saqie.projects.environment.application.port.out.EnvironmentRepository;
import github.saqie.projects.project.application.port.out.ProjectRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("environmentApplicationConfig")
class Config {

    @Bean
    CreateEnvironmentUseCase createEnvironmentUseCase(
            EnvironmentRepository environmentRepository,
            ProjectRepository projectRepository,
            OutboxWriter outboxWriter
    ) {
        return EnvironmentHandlerFacade.createEnvironmentUseCase(environmentRepository, projectRepository, outboxWriter);
    }

}
