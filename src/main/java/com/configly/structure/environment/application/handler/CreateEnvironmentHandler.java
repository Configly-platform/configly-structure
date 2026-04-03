package com.configly.structure.environment.application.handler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import com.configly.structure.environment.application.policy.EnvironmentPolicyFacade;
import com.configly.structure.environment.application.port.in.CreateEnvironmentUseCase;
import com.configly.structure.environment.application.port.in.command.CreateEnvironmentCommand;
import com.configly.structure.environment.application.port.out.EnvironmentCommandRepository;
import com.configly.structure.environment.domain.Environment;
import com.configly.model.environment.EnvironmentId;
import com.configly.outbox.api.OutboxEvent;
import com.configly.outbox.api.OutboxWriter;

import static com.configly.structure.environment.application.handler.EventMapper.createEnvironmentCreatedEvent;
import static com.configly.contracts.topic.KafkaTopic.CONFIGURATION;


@AllArgsConstructor
@Slf4j
class CreateEnvironmentHandler implements CreateEnvironmentUseCase {

    private final EnvironmentCommandRepository environmentCommandRepository;
    private final EnvironmentPolicyFacade environmentPolicyFacade;
    private final OutboxWriter outboxWriter;

    @Override
    @Transactional
    public EnvironmentId handle(CreateEnvironmentCommand command) {
        var environment = Environment.create(command);
        environmentPolicyFacade.ensureCreateAllowed(environment);

        var environmentId = environmentCommandRepository.save(environment);

        var event = createEnvironmentCreatedEvent(environment, command.actor(), command.correlationId());
        outboxWriter.write(OutboxEvent.of(command.projectId().idAsString(), event, CONFIGURATION));

        log.info("Environment created: environmentId={}", environment.id().uuid());
        return environmentId;
    }
}
