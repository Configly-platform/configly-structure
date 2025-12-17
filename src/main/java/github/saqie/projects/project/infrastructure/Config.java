package github.saqie.projects.project.infrastructure;

import github.saqie.ftaasoutbox.api.OutboxWriter;
import github.saqie.projects.project.application.handler.ProjectHandlerFacade;
import github.saqie.projects.project.application.port.in.CreateProjectUseCase;
import github.saqie.projects.project.application.port.out.ProjectRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("projectApplicationConfig")
class Config {

    @Bean
    CreateProjectUseCase createProjectUseCase(
            ProjectRepository projectRepository,
            OutboxWriter outboxWriter
    ) {
        return ProjectHandlerFacade.createProjectUseCase(projectRepository, outboxWriter);
    }

}
