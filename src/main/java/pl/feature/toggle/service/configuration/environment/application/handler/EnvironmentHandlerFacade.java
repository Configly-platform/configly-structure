package pl.feature.toggle.service.configuration.environment.application.handler;

import pl.feature.toggle.service.configuration.environment.application.policy.EnvironmentPolicyFacade;
import pl.feature.toggle.service.configuration.environment.application.port.in.ChangeEnvironmentStatusUseCase;
import pl.feature.toggle.service.configuration.environment.application.port.in.ChangeEnvironmentTypeUseCase;
import pl.feature.toggle.service.configuration.environment.application.port.in.CreateEnvironmentUseCase;
import pl.feature.toggle.service.configuration.environment.application.port.in.UpdateEnvironmentUseCase;
import pl.feature.toggle.service.configuration.environment.application.port.out.EnvironmentCommandRepository;
import pl.feature.toggle.service.configuration.environment.application.port.out.EnvironmentQueryRepository;
import pl.feature.toggle.service.configuration.project.application.port.out.ProjectQueryRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import pl.feature.toggle.service.model.security.actor.ActorProvider;
import pl.feature.toggle.service.model.security.correlation.CorrelationProvider;
import pl.feature.toggle.service.outbox.api.OutboxWriter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class EnvironmentHandlerFacade {

    public static CreateEnvironmentUseCase createEnvironmentUseCase(
            EnvironmentCommandRepository environmentCommandRepository,
            EnvironmentPolicyFacade environmentPolicyFacade,
            OutboxWriter outboxWriter,
            ActorProvider actorProvider,
            CorrelationProvider correlationProvider
    ) {
        return new CreateEnvironmentHandler(environmentCommandRepository, environmentPolicyFacade,
                outboxWriter, actorProvider, correlationProvider);
    }

    public static ChangeEnvironmentStatusUseCase changeEnvironmentStatusUseCase(
            EnvironmentCommandRepository environmentCommandRepository,
            EnvironmentQueryRepository environmentQueryRepository,
            EnvironmentPolicyFacade environmentPolicyFacade,
            OutboxWriter outboxWriter,
            ActorProvider actorProvider,
            CorrelationProvider correlationProvider
    ) {
        return new ChangeEnvironmentStatusHandler(environmentCommandRepository, environmentQueryRepository,
                environmentPolicyFacade, outboxWriter, actorProvider, correlationProvider);
    }

    public static UpdateEnvironmentUseCase updateEnvironmentUseCase(
            EnvironmentCommandRepository environmentCommandRepository,
            EnvironmentQueryRepository environmentQueryRepository,
            EnvironmentPolicyFacade environmentPolicyFacade,
            OutboxWriter outboxWriter,
            ActorProvider actorProvider,
            CorrelationProvider correlationProvider
    ) {
        return new UpdateEnvironmentHandler(environmentCommandRepository, environmentQueryRepository,
                environmentPolicyFacade, outboxWriter, actorProvider, correlationProvider);
    }

    public static ChangeEnvironmentTypeUseCase changeEnvironmentTypeUseCase(
            EnvironmentCommandRepository environmentCommandRepository,
            EnvironmentQueryRepository environmentQueryRepository,
            EnvironmentPolicyFacade environmentPolicyFacade,
            OutboxWriter outboxWriter,
            ActorProvider actorProvider,
            CorrelationProvider correlationProvider
    ) {
        return new ChangeEnvironmentTypeHandler(environmentCommandRepository, environmentQueryRepository,
                environmentPolicyFacade, outboxWriter, actorProvider, correlationProvider);
    }

}
