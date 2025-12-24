package pl.feature.toggle.service.configuration.project.infrastructure;

import pl.feature.toggle.service.configuration.project.application.handler.ProjectHandlerFacade;
import pl.feature.toggle.service.configuration.project.application.port.in.CreateProjectUseCase;
import pl.feature.toggle.service.configuration.project.application.port.out.ProjectRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.feature.toggle.service.model.security.ActorProvider;
import pl.feature.toggle.service.outbox.api.OutboxWriter;

@Configuration("projectApplicationConfig")
class Config {

    @Bean
    CreateProjectUseCase createProjectUseCase(
            ProjectRepository projectRepository,
            OutboxWriter outboxWriter,
            ActorProvider actorProvider
    ) {
        return ProjectHandlerFacade.createProjectUseCase(projectRepository, outboxWriter, actorProvider);
    }

}
