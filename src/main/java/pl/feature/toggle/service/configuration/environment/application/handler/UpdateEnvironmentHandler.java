package pl.feature.toggle.service.configuration.environment.application.handler;

import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import pl.feature.toggle.service.configuration.environment.application.policy.EnvironmentPolicyFacade;
import pl.feature.toggle.service.configuration.environment.application.port.in.UpdateEnvironmentUseCase;
import pl.feature.toggle.service.configuration.environment.application.port.in.command.UpdateEnvironmentCommand;
import pl.feature.toggle.service.configuration.environment.application.port.out.EnvironmentCommandRepository;
import pl.feature.toggle.service.configuration.environment.application.port.out.EnvironmentQueryRepository;
import pl.feature.toggle.service.model.security.actor.ActorProvider;
import pl.feature.toggle.service.model.security.correlation.CorrelationProvider;
import pl.feature.toggle.service.outbox.api.OutboxWriter;

import static pl.feature.toggle.service.configuration.environment.application.handler.EventMapper.createEnvironmentUpdatedEvent;
import static pl.feature.toggle.service.contracts.topic.KafkaTopic.PROJECT_ENV;

@AllArgsConstructor
class UpdateEnvironmentHandler implements UpdateEnvironmentUseCase {

    private final EnvironmentCommandRepository environmentCommandRepository;
    private final EnvironmentQueryRepository environmentQueryRepository;
    private final EnvironmentPolicyFacade environmentPolicyFacade;
    private final OutboxWriter outboxWriter;
    private final ActorProvider actorProvider;
    private final CorrelationProvider correlationProvider;

    @Override
    @Transactional
    public void handle(UpdateEnvironmentCommand command) {
        var environment = environmentQueryRepository.getOrThrow(command.environmentId(), command.projectId());
        environmentPolicyFacade.ensureUpdateAllowed(environment, command.name());

        var updateResult = environment.update(command.name());
        if (!updateResult.wasUpdated()) {
            return;
        }
        var updatedEnvironment = updateResult.environment();


        environmentCommandRepository.update(updatedEnvironment);

        var event = createEnvironmentUpdatedEvent(updateResult, actorProvider.current(), correlationProvider.current());
        outboxWriter.write(event, PROJECT_ENV.topic());
    }
}
