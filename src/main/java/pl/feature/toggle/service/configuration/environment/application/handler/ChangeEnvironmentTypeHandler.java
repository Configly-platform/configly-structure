package pl.feature.toggle.service.configuration.environment.application.handler;

import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import pl.feature.toggle.service.configuration.environment.application.policy.EnvironmentPolicyFacade;
import pl.feature.toggle.service.configuration.environment.application.port.in.ChangeEnvironmentTypeUseCase;
import pl.feature.toggle.service.configuration.environment.application.port.in.command.ChangeEnvironmentTypeCommand;
import pl.feature.toggle.service.configuration.environment.application.port.out.EnvironmentCommandRepository;
import pl.feature.toggle.service.configuration.environment.application.port.out.EnvironmentQueryRepository;
import pl.feature.toggle.service.configuration.environment.domain.EnvironmentUpdateResult;
import pl.feature.toggle.service.model.security.actor.ActorProvider;
import pl.feature.toggle.service.model.security.correlation.CorrelationProvider;
import pl.feature.toggle.service.outbox.api.OutboxWriter;

import static pl.feature.toggle.service.configuration.environment.application.handler.EventMapper.createEnvironmentStatusChangedEvent;
import static pl.feature.toggle.service.configuration.environment.application.handler.EventMapper.createEnvironmentTypeChangedEvent;
import static pl.feature.toggle.service.contracts.topic.KafkaTopic.PROJECT_ENV;

@AllArgsConstructor
class ChangeEnvironmentTypeHandler implements ChangeEnvironmentTypeUseCase {

    private final EnvironmentCommandRepository environmentCommandRepository;
    private final EnvironmentQueryRepository environmentQueryRepository;
    private final EnvironmentPolicyFacade environmentPolicyFacade;
    private final OutboxWriter outboxWriter;
    private final ActorProvider actorProvider;
    private final CorrelationProvider correlationProvider;

    @Override
    @Transactional
    public void handle(ChangeEnvironmentTypeCommand command) {
        var environment = environmentQueryRepository.getOrThrow(command.environmentId(), command.projectId());
        environmentPolicyFacade.ensureChangeTypeAllowed(environment);

        var updateResult = environment.changeType(command.type());
        if (!updateResult.wasUpdated()) {
            return;
        }

        var updatedEnvironment = updateResult.environment();
        environmentCommandRepository.update(updatedEnvironment);

        var event = createEnvironmentTypeChangedEvent(updateResult, actorProvider.current(), correlationProvider.current());
        outboxWriter.write(event, PROJECT_ENV.topic());
    }
}
