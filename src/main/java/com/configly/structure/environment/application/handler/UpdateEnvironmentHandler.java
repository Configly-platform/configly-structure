package com.configly.structure.environment.application.handler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import com.configly.structure.environment.application.policy.EnvironmentPolicyFacade;
import com.configly.structure.environment.application.port.in.UpdateEnvironmentUseCase;
import com.configly.structure.environment.application.port.in.command.UpdateEnvironmentCommand;
import com.configly.structure.environment.application.port.out.EnvironmentCommandRepository;
import com.configly.structure.environment.application.port.out.EnvironmentQueryRepository;
import com.configly.outbox.api.OutboxEvent;
import com.configly.outbox.api.OutboxWriter;

import static com.configly.structure.environment.application.handler.EventMapper.createEnvironmentUpdatedEvent;
import static com.configly.contracts.topic.KafkaTopic.CONFIGURATION;

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
