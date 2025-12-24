package pl.feature.toggle.service.configuration.environment.application.handler;

import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import pl.feature.toggle.service.configuration.environment.application.port.in.CreateEnvironmentCommand;
import pl.feature.toggle.service.configuration.environment.application.port.in.CreateEnvironmentUseCase;
import pl.feature.toggle.service.configuration.environment.application.port.out.EnvironmentRepository;
import pl.feature.toggle.service.configuration.environment.domain.Environment;
import pl.feature.toggle.service.configuration.environment.domain.exception.CannotCreateEnvironmentForMissingProject;
import pl.feature.toggle.service.configuration.environment.domain.exception.EnvironmentAlreadyExistsException;
import pl.feature.toggle.service.configuration.project.application.port.out.ProjectRepository;
import pl.feature.toggle.service.model.environment.EnvironmentId;
import pl.feature.toggle.service.model.project.ProjectId;
import pl.feature.toggle.service.model.security.ActorProvider;
import pl.feature.toggle.service.outbox.api.OutboxWriter;

import static pl.feature.toggle.service.configuration.environment.application.handler.EnvironmentHandlerEventMapper.createEnvironmentCreatedEvent;
import static pl.feature.toggle.service.contracts.topic.KafkaTopic.PROJECT_ENV;


@AllArgsConstructor
class CreateEnvironmentHandler implements CreateEnvironmentUseCase {

    private final EnvironmentRepository environmentRepository;
    private final ProjectRepository projectRepository;
    private final OutboxWriter outboxWriter;
    private final ActorProvider actorProvider;

    @Override
    @Transactional
    public EnvironmentId handle(CreateEnvironmentCommand command) {
        var environment = Environment.create(command);

        validate(environment);

        var environmentId = environmentRepository.save(environment);

        var event = createEnvironmentCreatedEvent(environment, actorProvider.current());
        outboxWriter.write(event, PROJECT_ENV.topic());

        return environmentId;
    }

    private void validate(Environment environment) {
        validateProjectExists(environment.projectId());
        validateUniqueProjectAndName(environment);
    }

    private void validateUniqueProjectAndName(Environment environment) {
        if (environmentRepository.existsByProjectIdAndName(environment.projectId(), environment.name())) {
            throw new EnvironmentAlreadyExistsException(environment);
        }
    }

    private void validateProjectExists(ProjectId projectId) {
        var projectExists = projectRepository.exists(projectId);
        if (!projectExists) {
            throw new CannotCreateEnvironmentForMissingProject(projectId);
        }
    }

}
