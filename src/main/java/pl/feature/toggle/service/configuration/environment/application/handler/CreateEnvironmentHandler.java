package pl.feature.toggle.service.configuration.environment.application.handler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import pl.feature.toggle.service.configuration.environment.application.policy.EnvironmentPolicyFacade;
import pl.feature.toggle.service.configuration.environment.application.port.in.CreateEnvironmentUseCase;
import pl.feature.toggle.service.configuration.environment.application.port.in.command.CreateEnvironmentCommand;
import pl.feature.toggle.service.configuration.environment.application.port.out.EnvironmentCommandRepository;
import pl.feature.toggle.service.configuration.environment.domain.Environment;
import pl.feature.toggle.service.model.environment.EnvironmentId;
import pl.feature.toggle.service.outbox.api.OutboxEvent;
import pl.feature.toggle.service.web.actor.ActorProvider;
import pl.feature.toggle.service.web.correlation.CorrelationProvider;
import pl.feature.toggle.service.outbox.api.OutboxWriter;

import static pl.feature.toggle.service.configuration.environment.application.handler.EventMapper.createEnvironmentCreatedEvent;
import static pl.feature.toggle.service.contracts.topic.KafkaTopic.CONFIGURATION;


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
