package pl.feature.toggle.service.configuration.project.infrastructure;

import pl.feature.toggle.service.configuration.project.application.handler.ProjectHandlerFacade;
import pl.feature.toggle.service.configuration.project.application.port.in.ChangeProjectStatusUseCase;
import pl.feature.toggle.service.configuration.project.application.port.in.CreateProjectUseCase;
import pl.feature.toggle.service.configuration.project.application.port.in.UpdateProjectUseCase;
import pl.feature.toggle.service.configuration.project.application.port.out.EnvironmentStatusCascadePort;
import pl.feature.toggle.service.configuration.project.application.port.out.ProjectRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.feature.toggle.service.model.security.actor.ActorProvider;
import pl.feature.toggle.service.model.security.correlation.CorrelationProvider;
import pl.feature.toggle.service.outbox.api.OutboxWriter;

@Configuration("projectApplicationConfig")
class Config {

    @Bean
    CreateProjectUseCase createProjectUseCase(
            ProjectRepository projectRepository,
            OutboxWriter outboxWriter,
            ActorProvider actorProvider,
            CorrelationProvider correlationProvider
    ) {
        return ProjectHandlerFacade.createProjectUseCase(projectRepository, outboxWriter, actorProvider, correlationProvider);
    }

    @Bean
    UpdateProjectUseCase updateProjectUseCase(
            ProjectRepository projectRepository,
            OutboxWriter outboxWriter,
            ActorProvider actorProvider,
            CorrelationProvider correlationProvider
    ) {
        return ProjectHandlerFacade.updateProjectUseCase(projectRepository, outboxWriter, actorProvider, correlationProvider);
    }

    @Bean
    ChangeProjectStatusUseCase changeProjectStatusUseCase(
            ProjectRepository projectRepository,
            OutboxWriter outboxWriter,
            ActorProvider actorProvider,
            CorrelationProvider correlationProvider,
            EnvironmentStatusCascadePort environmentStatusCascadePort
    ) {
        return ProjectHandlerFacade.changeProjectStatusUseCase(projectRepository, outboxWriter,
                actorProvider, correlationProvider, environmentStatusCascadePort);
    }

}
