package com.configly.structure.project.application.handler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import com.configly.structure.project.application.port.in.CreateProjectUseCase;
import com.configly.structure.project.application.port.in.command.CreateProjectCommand;
import com.configly.structure.project.application.port.out.ProjectCommandRepository;
import com.configly.structure.project.domain.Project;
import com.configly.model.project.ProjectId;
import com.configly.outbox.api.OutboxEvent;
import com.configly.outbox.api.OutboxWriter;

import static com.configly.structure.project.application.handler.EventMapper.createProjectCreatedEvent;
import static com.configly.contracts.topic.KafkaTopic.CONFIGURATION;

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
