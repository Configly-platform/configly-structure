package com.configly.structure.project.application.handler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.configly.structure.project.application.port.in.UpdateProjectUseCase;
import com.configly.structure.project.application.port.in.command.UpdateProjectCommand;
import com.configly.structure.project.application.port.out.ProjectCommandRepository;
import com.configly.structure.project.application.port.out.ProjectQueryRepository;
import com.configly.outbox.api.OutboxEvent;
import com.configly.outbox.api.OutboxWriter;

import static com.configly.structure.project.application.handler.EventMapper.createProjectUpdatedEvent;
import static com.configly.contracts.topic.KafkaTopic.CONFIGURATION;

@AllArgsConstructor
@Slf4j
class UpdateProjectHandler implements UpdateProjectUseCase {

    private final ProjectCommandRepository projectCommandRepository;
    private final ProjectQueryRepository projectQueryRepository;
    private final OutboxWriter outboxWriter;

    @Override
    public void handle(UpdateProjectCommand command) {
        var project = projectQueryRepository.getOrThrow(command.projectId());

        var updateResult = project.update(
                command.name(),
                command.description()
        );
        if (!updateResult.wasUpdated()) {
            return;
        }

        projectCommandRepository.update(updateResult);

        var event = createProjectUpdatedEvent(updateResult, command.actor(), command.correlationId());
        outboxWriter.write(OutboxEvent.of(project.id().idAsString(), event, CONFIGURATION));

        log.info("Project updated: projectId={}, newName={}, newDescription={}",
                project.id().uuid(), command.name().value(), command.description().value());
    }
}
