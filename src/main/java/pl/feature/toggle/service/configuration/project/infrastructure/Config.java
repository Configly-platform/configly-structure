package pl.feature.toggle.service.configuration.project.infrastructure;

import pl.feature.toggle.service.configuration.project.application.handler.ProjectHandlerFacade;
import pl.feature.toggle.service.configuration.project.application.port.in.ChangeProjectStatusUseCase;
import pl.feature.toggle.service.configuration.project.application.port.in.CreateProjectUseCase;
import pl.feature.toggle.service.configuration.project.application.port.in.UpdateProjectUseCase;
import pl.feature.toggle.service.configuration.project.application.port.out.EnvironmentStatusCascadePort;
import pl.feature.toggle.service.configuration.project.application.port.out.ProjectCommandRepository;
import pl.feature.toggle.service.configuration.project.application.port.out.ProjectQueryRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.feature.toggle.service.model.security.actor.ActorProvider;
import pl.feature.toggle.service.model.security.correlation.CorrelationProvider;
import pl.feature.toggle.service.outbox.api.OutboxWriter;

@Configuration("projectApplicationConfig")
class Config {

    @Bean
    CreateProjectUseCase createProjectUseCase(
            ProjectCommandRepository projectCommandRepository,
            ProjectQueryRepository projectQueryRepository,
            OutboxWriter outboxWriter,
            ActorProvider actorProvider,
            CorrelationProvider correlationProvider
    ) {
        return ProjectHandlerFacade.createProjectUseCase(projectCommandRepository, projectQueryRepository,
                outboxWriter, actorProvider, correlationProvider);
    }

    @Bean
    UpdateProjectUseCase updateProjectUseCase(
            ProjectCommandRepository projectCommandRepository,
            ProjectQueryRepository projectQueryRepository,
            OutboxWriter outboxWriter,
            ActorProvider actorProvider,
            CorrelationProvider correlationProvider
    ) {
        return ProjectHandlerFacade.updateProjectUseCase(projectCommandRepository, projectQueryRepository,
                outboxWriter, actorProvider, correlationProvider);
    }

    @Bean
    ChangeProjectStatusUseCase changeProjectStatusUseCase(
            ProjectCommandRepository projectCommandRepository,
            ProjectQueryRepository projectQueryRepository,
            OutboxWriter outboxWriter,
            ActorProvider actorProvider,
            CorrelationProvider correlationProvider,
            EnvironmentStatusCascadePort environmentStatusCascadePort
    ) {
        return ProjectHandlerFacade.changeProjectStatusUseCase(projectCommandRepository, projectQueryRepository, outboxWriter,
                actorProvider, correlationProvider, environmentStatusCascadePort);
    }

}
