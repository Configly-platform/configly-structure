package pl.feature.toggle.service.configuration.project.application.handler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.feature.toggle.service.configuration.project.application.port.in.UpdateProjectUseCase;
import pl.feature.toggle.service.configuration.project.application.port.in.command.UpdateProjectCommand;
import pl.feature.toggle.service.configuration.project.application.port.out.ProjectCommandRepository;
import pl.feature.toggle.service.configuration.project.application.port.out.ProjectQueryRepository;
import pl.feature.toggle.service.outbox.api.OutboxEvent;
import pl.feature.toggle.service.outbox.api.OutboxWriter;

import static pl.feature.toggle.service.configuration.project.application.handler.EventMapper.createProjectUpdatedEvent;
import static pl.feature.toggle.service.contracts.topic.KafkaTopic.CONFIGURATION;

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
