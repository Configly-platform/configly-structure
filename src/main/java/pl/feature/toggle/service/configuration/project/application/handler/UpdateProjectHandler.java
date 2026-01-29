package pl.feature.toggle.service.configuration.project.application.handler;

import lombok.AllArgsConstructor;
import pl.feature.toggle.service.configuration.project.application.port.in.UpdateProjectUseCase;
import pl.feature.toggle.service.configuration.project.application.port.in.command.UpdateProjectCommand;
import pl.feature.toggle.service.configuration.project.application.port.out.ProjectRepository;
import pl.feature.toggle.service.configuration.project.domain.Project;
import pl.feature.toggle.service.configuration.project.domain.exception.ProjectNotFoundException;
import pl.feature.toggle.service.model.project.ProjectId;
import pl.feature.toggle.service.model.security.actor.ActorProvider;
import pl.feature.toggle.service.model.security.correlation.CorrelationProvider;
import pl.feature.toggle.service.outbox.api.OutboxWriter;

import static pl.feature.toggle.service.configuration.project.application.handler.EventMapper.createProjectUpdatedEvent;
import static pl.feature.toggle.service.contracts.topic.KafkaTopic.PROJECT_ENV;

@AllArgsConstructor
class UpdateProjectHandler implements UpdateProjectUseCase {

    private final ProjectRepository projectRepository;
    private final ActorProvider actorProvider;
    private final CorrelationProvider correlationProvider;
    private final OutboxWriter outboxWriter;

    @Override
    public void handle(UpdateProjectCommand command) {
        var project = loadProject(command.projectId());

        var updateResult = project.update(
                command.name(),
                command.description()
        );

        if (!updateResult.wasUpdated()) {
            return;
        }

        projectRepository.update(updateResult.project());

        var event = createProjectUpdatedEvent(updateResult, actorProvider.current(), correlationProvider.current());
        outboxWriter.write(event, PROJECT_ENV.topic());
    }

    private Project loadProject(ProjectId projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));
    }
}
