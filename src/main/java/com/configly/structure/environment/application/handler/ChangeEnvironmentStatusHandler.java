package com.configly.structure.environment.application.handler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import com.configly.structure.environment.application.policy.EnvironmentPolicyFacade;
import com.configly.structure.environment.application.port.in.ChangeEnvironmentStatusUseCase;
import com.configly.structure.environment.application.port.in.command.ChangeEnvironmentStatusCommand;
import com.configly.structure.environment.application.port.out.EnvironmentCommandRepository;
import com.configly.structure.environment.application.port.out.EnvironmentQueryRepository;
import com.configly.structure.environment.domain.Environment;
import com.configly.structure.environment.domain.EnvironmentUpdateResult;
import com.configly.model.environment.EnvironmentStatus;
import com.configly.outbox.api.OutboxEvent;
import com.configly.outbox.api.OutboxWriter;

import static com.configly.structure.environment.application.handler.EventMapper.createEnvironmentStatusChangedEvent;
import static com.configly.contracts.topic.KafkaTopic.CONFIGURATION;

@AllArgsConstructor
@Slf4j
class ChangeEnvironmentStatusHandler implements ChangeEnvironmentStatusUseCase {

    private final EnvironmentCommandRepository environmentCommandRepository;
    private final EnvironmentQueryRepository environmentQueryRepository;
    private final EnvironmentPolicyFacade environmentPolicyFacade;
    private final OutboxWriter outboxWriter;

    @Override
    @Transactional
    public void handle(ChangeEnvironmentStatusCommand command) {
        var environment = environmentQueryRepository.getOrThrow(command.projectId(), command.environmentId());
        environmentPolicyFacade.ensureChangeStatusAllowed(environment);

        var updateResult = changeStatus(environment, command.newEnvironmentStatus());
        if (!updateResult.wasUpdated()) {
            return;
        }

        environmentCommandRepository.update(updateResult);

        var event = createEnvironmentStatusChangedEvent(updateResult, command.actor(), command.correlationId());
        outboxWriter.write(OutboxEvent.of(command.projectId().idAsString(), event, CONFIGURATION));
        log.info("Environment status changed: environmentId={}, oldStatus={}, newStatus={}", environment.id().uuid(),
                environment.status(), updateResult.environment().status());
    }

    private EnvironmentUpdateResult changeStatus(Environment environment, EnvironmentStatus environmentStatus) {
        return switch (environmentStatus) {
            case ACTIVE -> environment.restore();
            case ARCHIVED -> environment.archive();
        };
    }
}
