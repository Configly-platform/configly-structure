package pl.feature.toggle.service.configuration.project.application.handler;

import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import pl.feature.toggle.service.configuration.project.application.port.in.ChangeProjectStatusUseCase;
import pl.feature.toggle.service.configuration.project.application.port.in.command.ChangeStatusCommand;
import pl.feature.toggle.service.configuration.project.application.port.out.EnvironmentStatusCascadePort;
import pl.feature.toggle.service.configuration.project.application.port.out.ProjectRepository;
import pl.feature.toggle.service.configuration.project.domain.Project;
import pl.feature.toggle.service.configuration.project.domain.ProjectUpdateResult;
import pl.feature.toggle.service.configuration.project.domain.Status;
import pl.feature.toggle.service.configuration.project.domain.exception.ProjectNotFoundException;
import pl.feature.toggle.service.model.project.ProjectId;
import pl.feature.toggle.service.model.security.actor.ActorProvider;
import pl.feature.toggle.service.model.security.correlation.CorrelationProvider;
import pl.feature.toggle.service.outbox.api.OutboxWriter;

import static pl.feature.toggle.service.configuration.project.application.handler.EventMapper.createProjectStatusChangedEvent;
import static pl.feature.toggle.service.contracts.topic.KafkaTopic.PROJECT_ENV;

@AllArgsConstructor
class ChangeProjectStatusHandler implements ChangeProjectStatusUseCase {

    private final ProjectRepository projectRepository;
    private final ActorProvider actorProvider;
    private final CorrelationProvider correlationProvider;
    private final OutboxWriter outboxWriter;
    private final EnvironmentStatusCascadePort environmentStatusCascadePort;

    @Override
    @Transactional
    public void handle(ChangeStatusCommand command) {
        var project = loadProject(command.projectId());

        var updateResult = changeStatus(project, command.newStatus());
        if (!updateResult.wasUpdated()) {
            return;
        }

        var updatedProject = updateResult.project();

        projectRepository.update(updatedProject);
        changeEnvironmentsStatus(updatedProject);

        var event = createProjectStatusChangedEvent(updateResult, actorProvider.current(), correlationProvider.current());
        outboxWriter.write(event, PROJECT_ENV.topic());
    }

    private void changeEnvironmentsStatus(Project project) {
        if (project.isActive()) {
            environmentStatusCascadePort.restoreCascadeByProjectId(project.id());
            return;
        }
        environmentStatusCascadePort.archiveCascadeByProjectId(project.id());
    }

    private ProjectUpdateResult changeStatus(Project project, Status newStatus) {
        return switch (newStatus) {
            case ACTIVE -> project.restore();
            case ARCHIVED -> project.archive();
        };
    }

    private Project loadProject(ProjectId projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));
    }
}
