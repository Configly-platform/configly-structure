package pl.feature.toggle.service.configuration.environment.application.handler;

import com.ftaas.domain.environment.EnvironmentId;
import com.ftaas.domain.project.ProjectId;
import github.saqie.ftaasoutbox.api.OutboxWriter;
import pl.feature.toggle.service.configuration.environment.application.port.in.CreateEnvironmentCommand;
import pl.feature.toggle.service.configuration.environment.application.port.in.CreateEnvironmentUseCase;
import pl.feature.toggle.service.configuration.environment.application.port.out.EnvironmentRepository;
import pl.feature.toggle.service.configuration.environment.domain.Environment;
import pl.feature.toggle.service.configuration.environment.domain.exception.CannotCreateEnvironmentForMissingProject;
import pl.feature.toggle.service.configuration.environment.domain.exception.EnvironmentAlreadyExistsException;
import pl.feature.toggle.service.configuration.project.application.port.out.ProjectRepository;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import static com.ftaas.contracts.topic.KafkaTopic.PROJECT_ENV;
import static pl.feature.toggle.service.configuration.environment.application.handler.EnvironmentHandlerEventMapper.createEnvironmentCreatedEvent;


@AllArgsConstructor
class CreateEnvironmentHandler implements CreateEnvironmentUseCase {

    private final EnvironmentRepository environmentRepository;
    private final ProjectRepository projectRepository;
    private final OutboxWriter outboxWriter;

    @Override
    @Transactional
    public EnvironmentId handle(CreateEnvironmentCommand command) {
        var environment = Environment.create(command);

        validate(environment);

        var environmentId = environmentRepository.save(environment);

        var event = createEnvironmentCreatedEvent(environment);
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
