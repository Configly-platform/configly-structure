package com.configly.structure.environment.application.handler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import com.configly.structure.environment.application.policy.EnvironmentPolicyFacade;
import com.configly.structure.environment.application.port.in.ChangeEnvironmentTypeUseCase;
import com.configly.structure.environment.application.port.in.command.ChangeEnvironmentTypeCommand;
import com.configly.structure.environment.application.port.out.EnvironmentCommandRepository;
import com.configly.structure.environment.application.port.out.EnvironmentQueryRepository;
import com.configly.outbox.api.OutboxEvent;
import com.configly.outbox.api.OutboxWriter;

import static com.configly.structure.environment.application.handler.EventMapper.createEnvironmentTypeChangedEvent;
import static com.configly.contracts.topic.KafkaTopic.CONFIGURATION;

@AllArgsConstructor
@Slf4j
class ChangeEnvironmentTypeHandler implements ChangeEnvironmentTypeUseCase {

    private final EnvironmentCommandRepository environmentCommandRepository;
    private final EnvironmentQueryRepository environmentQueryRepository;
    private final EnvironmentPolicyFacade environmentPolicyFacade;
    private final OutboxWriter outboxWriter;

    @Override
    @Transactional
    public void handle(ChangeEnvironmentTypeCommand command) {
        var environment = environmentQueryRepository.getOrThrow(command.projectId(), command.environmentId());
        environmentPolicyFacade.ensureChangeTypeAllowed(environment);

        var updateResult = environment.changeType(command.type());
        if (!updateResult.wasUpdated()) {
            return;
        }

        environmentCommandRepository.update(updateResult);

        var event = createEnvironmentTypeChangedEvent(updateResult, command.actor(), command.correlationId());
        outboxWriter.write(OutboxEvent.of(command.projectId().idAsString(), event, CONFIGURATION));
        log.info("Environment type changed: environmentId={}, newType={}", environment.id().uuid(), environment.type());
    }
}
