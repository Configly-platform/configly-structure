package pl.feature.toggle.service.configuration.environment.application.handler;

import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import pl.feature.toggle.service.configuration.environment.application.policy.EnvironmentPolicyFacade;
import pl.feature.toggle.service.configuration.environment.application.port.in.ChangeEnvironmentStatusUseCase;
import pl.feature.toggle.service.configuration.environment.application.port.in.command.ChangeEnvironmentStatusCommand;
import pl.feature.toggle.service.configuration.environment.application.port.out.EnvironmentCommandRepository;
import pl.feature.toggle.service.configuration.environment.application.port.out.EnvironmentQueryRepository;
import pl.feature.toggle.service.configuration.environment.domain.Environment;
import pl.feature.toggle.service.configuration.environment.domain.EnvironmentUpdateResult;
import pl.feature.toggle.service.model.environment.EnvironmentStatus;
import pl.feature.toggle.service.model.security.actor.ActorProvider;
import pl.feature.toggle.service.model.security.correlation.CorrelationProvider;
import pl.feature.toggle.service.outbox.api.OutboxWriter;

import static pl.feature.toggle.service.configuration.environment.application.handler.EventMapper.createEnvironmentStatusChangedEvent;
import static pl.feature.toggle.service.contracts.topic.KafkaTopic.CONFIGURATION;

@AllArgsConstructor
class ChangeEnvironmentStatusHandler implements ChangeEnvironmentStatusUseCase {

    private final EnvironmentCommandRepository environmentCommandRepository;
    private final EnvironmentQueryRepository environmentQueryRepository;
    private final EnvironmentPolicyFacade environmentPolicyFacade;
    private final OutboxWriter outboxWriter;
    private final ActorProvider actorProvider;
    private final CorrelationProvider correlationProvider;

    @Override
    @Transactional
    public void handle(ChangeEnvironmentStatusCommand command) {
        var environment = environmentQueryRepository.getOrThrow(command.environmentId(), command.projectId());
        environmentPolicyFacade.ensureChangeStatusAllowed(environment);

        var updateResult = changeStatus(environment, command.newEnvironmentStatus());
        if (!updateResult.wasUpdated()) {
            return;
        }

        environmentCommandRepository.update(updateResult);

        var event = createEnvironmentStatusChangedEvent(updateResult, actorProvider.current(), correlationProvider.current());
        outboxWriter.write(event, CONFIGURATION.topic());
    }

    private EnvironmentUpdateResult changeStatus(Environment environment, EnvironmentStatus environmentStatus) {
        return switch (environmentStatus) {
            case ACTIVE -> environment.restore();
            case ARCHIVED -> environment.archive();
        };
    }
}
