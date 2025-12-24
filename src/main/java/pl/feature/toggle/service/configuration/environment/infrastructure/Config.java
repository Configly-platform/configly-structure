package pl.feature.toggle.service.configuration.environment.infrastructure;

import pl.feature.toggle.service.configuration.environment.application.handler.EnvironmentHandlerFacade;
import pl.feature.toggle.service.configuration.environment.application.port.in.CreateEnvironmentUseCase;
import pl.feature.toggle.service.configuration.environment.application.port.out.EnvironmentRepository;
import pl.feature.toggle.service.configuration.project.application.port.out.ProjectRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.feature.toggle.service.model.security.ActorProvider;
import pl.feature.toggle.service.outbox.api.OutboxWriter;

@Configuration("environmentApplicationConfig")
class Config {

    @Bean
    CreateEnvironmentUseCase createEnvironmentUseCase(
            EnvironmentRepository environmentRepository,
            ProjectRepository projectRepository,
            OutboxWriter outboxWriter,
            ActorProvider actorProvider
    ) {
        return EnvironmentHandlerFacade.createEnvironmentUseCase(environmentRepository, projectRepository, outboxWriter, actorProvider);
    }

}
