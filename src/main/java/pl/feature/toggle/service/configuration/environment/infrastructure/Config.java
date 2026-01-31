package pl.feature.toggle.service.configuration.environment.infrastructure;

import pl.feature.toggle.service.configuration.environment.application.handler.EnvironmentHandlerFacade;
import pl.feature.toggle.service.configuration.environment.application.policy.EnvironmentPolicyFacade;
import pl.feature.toggle.service.configuration.environment.application.port.in.ChangeEnvironmentStatusUseCase;
import pl.feature.toggle.service.configuration.environment.application.port.in.ChangeEnvironmentTypeUseCase;
import pl.feature.toggle.service.configuration.environment.application.port.in.CreateEnvironmentUseCase;
import pl.feature.toggle.service.configuration.environment.application.port.in.UpdateEnvironmentUseCase;
import pl.feature.toggle.service.configuration.environment.application.port.out.EnvironmentCommandRepository;
import pl.feature.toggle.service.configuration.environment.application.port.out.EnvironmentQueryRepository;
import pl.feature.toggle.service.configuration.project.application.port.out.ProjectQueryRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.feature.toggle.service.model.security.actor.ActorProvider;
import pl.feature.toggle.service.model.security.correlation.CorrelationProvider;
import pl.feature.toggle.service.outbox.api.OutboxWriter;

@Configuration("environmentApplicationConfig")
class Config {

    @Bean
    CreateEnvironmentUseCase createEnvironmentUseCase(
            EnvironmentCommandRepository environmentCommandRepository,
            EnvironmentPolicyFacade environmentPolicyFacade,
            OutboxWriter outboxWriter,
            ActorProvider actorProvider,
            CorrelationProvider correlationProvider
    ) {
        return EnvironmentHandlerFacade.createEnvironmentUseCase(environmentCommandRepository, environmentPolicyFacade,
                outboxWriter, actorProvider, correlationProvider);
    }

    @Bean
    UpdateEnvironmentUseCase updateEnvironmentUseCase(
            EnvironmentCommandRepository environmentCommandRepository,
            EnvironmentQueryRepository environmentQueryRepository,
            EnvironmentPolicyFacade environmentPolicyFacade,
            OutboxWriter outboxWriter,
            ActorProvider actorProvider,
            CorrelationProvider correlationProvider
    ) {
        return EnvironmentHandlerFacade.updateEnvironmentUseCase(environmentCommandRepository,
                environmentQueryRepository, environmentPolicyFacade, outboxWriter, actorProvider, correlationProvider);
    }

    @Bean
    ChangeEnvironmentTypeUseCase changeEnvironmentTypeUseCase(
            EnvironmentCommandRepository environmentCommandRepository,
            EnvironmentQueryRepository environmentQueryRepository,
            EnvironmentPolicyFacade environmentPolicyFacade,
            OutboxWriter outboxWriter,
            ActorProvider actorProvider,
            CorrelationProvider correlationProvider
    ) {
        return EnvironmentHandlerFacade.changeEnvironmentTypeUseCase(environmentCommandRepository, environmentQueryRepository,
                environmentPolicyFacade, outboxWriter, actorProvider, correlationProvider);
    }

    @Bean
    ChangeEnvironmentStatusUseCase changeEnvironmentStatusUseCase(
            EnvironmentCommandRepository environmentCommandRepository,
            EnvironmentQueryRepository environmentQueryRepository,
            EnvironmentPolicyFacade environmentPolicyFacade,
            OutboxWriter outboxWriter,
            ActorProvider actorProvider,
            CorrelationProvider correlationProvider
    ) {
        return EnvironmentHandlerFacade.changeEnvironmentStatusUseCase(environmentCommandRepository, environmentQueryRepository,
                environmentPolicyFacade, outboxWriter, actorProvider, correlationProvider);
    }
}
