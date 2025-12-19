package pl.feature.toggle.service.configuration.environment.infrastructure;

import github.saqie.ftaasoutbox.api.OutboxWriter;
import pl.feature.toggle.service.configuration.environment.application.handler.EnvironmentHandlerFacade;
import pl.feature.toggle.service.configuration.environment.application.port.in.CreateEnvironmentUseCase;
import pl.feature.toggle.service.configuration.environment.application.port.out.EnvironmentRepository;
import pl.feature.toggle.service.configuration.project.application.port.out.ProjectRepository;
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
