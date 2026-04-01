package pl.feature.toggle.service.configuration.environment.application.handler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import pl.feature.toggle.service.configuration.environment.application.policy.EnvironmentPolicyFacade;
import pl.feature.toggle.service.configuration.environment.application.port.in.UpdateEnvironmentUseCase;
import pl.feature.toggle.service.configuration.environment.application.port.in.command.UpdateEnvironmentCommand;
import pl.feature.toggle.service.configuration.environment.application.port.out.EnvironmentCommandRepository;
import pl.feature.toggle.service.configuration.environment.application.port.out.EnvironmentQueryRepository;
import pl.feature.toggle.service.outbox.api.OutboxEvent;
import pl.feature.toggle.service.outbox.api.OutboxWriter;

import static pl.feature.toggle.service.configuration.environment.application.handler.EventMapper.createEnvironmentUpdatedEvent;
import static pl.feature.toggle.service.contracts.topic.KafkaTopic.CONFIGURATION;

@AllArgsConstructor
@Slf4j
class UpdateEnvironmentHandler implements UpdateEnvironmentUseCase {

    private final EnvironmentCommandRepository environmentCommandRepository;
    private final EnvironmentQueryRepository environmentQueryRepository;
    private final EnvironmentPolicyFacade environmentPolicyFacade;
    private final OutboxWriter outboxWriter;

    @Override
    @Transactional
    public void handle(UpdateEnvironmentCommand command) {
        var environment = environmentQueryRepository.getOrThrow(command.projectId(), command.environmentId());
        environmentPolicyFacade.ensureUpdateAllowed(environment);

        var updateResult = environment.update(command.name());
        if (!updateResult.wasUpdated()) {
            return;
        }

        environmentCommandRepository.update(updateResult);

        var event = createEnvironmentUpdatedEvent(updateResult, command.actor(), command.correlationId());
        outboxWriter.write(OutboxEvent.of(command.projectId().idAsString(), event, CONFIGURATION));
        log.info("Environment updated: environmentId={}, newName={}", environment.id().uuid(), environment.name().value());
    }
}
