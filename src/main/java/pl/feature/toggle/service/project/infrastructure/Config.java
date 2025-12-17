package pl.feature.toggle.service.project.infrastructure;

import github.saqie.ftaasoutbox.api.OutboxWriter;
import pl.feature.toggle.service.project.application.handler.ProjectHandlerFacade;
import pl.feature.toggle.service.project.application.port.in.CreateProjectUseCase;
import pl.feature.toggle.service.project.application.port.out.ProjectRepository;
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
