package pl.feature.toggle.service.configuration.project.application.handler;

import pl.feature.toggle.service.configuration.project.application.policy.ProjectPolicyFacade;
import pl.feature.toggle.service.configuration.project.application.port.in.command.CreateProjectCommand;
import pl.feature.toggle.service.configuration.project.application.port.in.CreateProjectUseCase;
import pl.feature.toggle.service.configuration.project.application.port.out.ProjectCommandRepository;
import pl.feature.toggle.service.configuration.project.application.port.out.ProjectQueryRepository;
import pl.feature.toggle.service.configuration.project.domain.Project;
import pl.feature.toggle.service.configuration.project.domain.exception.ProjectAlreadyExistsException;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import pl.feature.toggle.service.model.project.ProjectId;
import pl.feature.toggle.service.model.security.actor.ActorProvider;
import pl.feature.toggle.service.model.security.correlation.CorrelationProvider;
import pl.feature.toggle.service.outbox.api.OutboxWriter;

import static pl.feature.toggle.service.configuration.project.application.handler.EventMapper.createProjectCreatedEvent;
import static pl.feature.toggle.service.contracts.topic.KafkaTopic.PROJECT_ENV;

@AllArgsConstructor
class CreateProjectHandler implements CreateProjectUseCase {

    private final ProjectCommandRepository projectCommandRepository;
    private final ProjectQueryRepository projectQueryRepository;
    private final ProjectPolicyFacade projectPolicyFacade;
    private final OutboxWriter outboxWriter;
    private final ActorProvider actorProvider;
    private final CorrelationProvider correlationProvider;

    @Override
    @Transactional
    public ProjectId handle(CreateProjectCommand command) {
        var project = Project.create(command.name(), command.description());
        projectPolicyFacade.ensureCreateAllowed(project.name());

        projectCommandRepository.save(project);

        var event = createProjectCreatedEvent(project, actorProvider.current(), correlationProvider.current());
        outboxWriter.write(event, PROJECT_ENV.topic());

        return project.id();
    }
}
