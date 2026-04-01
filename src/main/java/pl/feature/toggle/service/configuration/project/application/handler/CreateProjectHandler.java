package pl.feature.toggle.service.configuration.project.application.handler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import pl.feature.toggle.service.configuration.project.application.port.in.CreateProjectUseCase;
import pl.feature.toggle.service.configuration.project.application.port.in.command.CreateProjectCommand;
import pl.feature.toggle.service.configuration.project.application.port.out.ProjectCommandRepository;
import pl.feature.toggle.service.configuration.project.domain.Project;
import pl.feature.toggle.service.model.project.ProjectId;
import pl.feature.toggle.service.outbox.api.OutboxEvent;
import pl.feature.toggle.service.outbox.api.OutboxWriter;

import static pl.feature.toggle.service.configuration.project.application.handler.EventMapper.createProjectCreatedEvent;
import static pl.feature.toggle.service.contracts.topic.KafkaTopic.CONFIGURATION;

@AllArgsConstructor
@Slf4j
class CreateProjectHandler implements CreateProjectUseCase {

    private final ProjectCommandRepository projectCommandRepository;
    private final OutboxWriter outboxWriter;

    @Override
    @Transactional
    public ProjectId handle(CreateProjectCommand command) {
        var project = Project.create(command.name(), command.description());

        projectCommandRepository.save(project);

        var event = createProjectCreatedEvent(project, command.actor(), command.correlationId());
        outboxWriter.write(OutboxEvent.of(project.id().idAsString(), event, CONFIGURATION));
        log.info("Project created: projectId={}", project.id().uuid());

        return project.id();
    }
}
