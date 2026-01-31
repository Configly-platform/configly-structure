package pl.feature.toggle.service.configuration.project.application.handler;

import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import pl.feature.toggle.service.configuration.project.application.port.in.ChangeProjectStatusUseCase;
import pl.feature.toggle.service.configuration.project.application.port.in.command.ChangeProjectStatusCommand;
import pl.feature.toggle.service.configuration.project.application.port.out.EnvironmentStatusCascadePort;
import pl.feature.toggle.service.configuration.project.application.port.out.ProjectCommandRepository;
import pl.feature.toggle.service.configuration.project.application.port.out.ProjectQueryRepository;
import pl.feature.toggle.service.configuration.project.domain.Project;
import pl.feature.toggle.service.configuration.project.domain.ProjectUpdateResult;
import pl.feature.toggle.service.configuration.project.domain.ProjectStatus;
import pl.feature.toggle.service.configuration.project.domain.exception.ProjectNotFoundException;
import pl.feature.toggle.service.model.project.ProjectId;
import pl.feature.toggle.service.model.security.actor.ActorProvider;
import pl.feature.toggle.service.model.security.correlation.CorrelationProvider;
import pl.feature.toggle.service.outbox.api.OutboxWriter;

import static pl.feature.toggle.service.configuration.project.application.handler.EventMapper.createProjectStatusChangedEvent;
import static pl.feature.toggle.service.contracts.topic.KafkaTopic.PROJECT_ENV;

@AllArgsConstructor
class ChangeProjectStatusHandler implements ChangeProjectStatusUseCase {

    private final ProjectCommandRepository projectCommandRepository;
    private final ProjectQueryRepository projectQueryRepository;
    private final ActorProvider actorProvider;
    private final CorrelationProvider correlationProvider;
    private final OutboxWriter outboxWriter;
    private final EnvironmentStatusCascadePort environmentStatusCascadePort;

    @Override
    @Transactional
    public void handle(ChangeProjectStatusCommand command) {
        var project = projectQueryRepository.getOrThrow(command.projectId());

        var updateResult = changeStatus(project, command.newProjectStatus());
        if (!updateResult.wasUpdated()) {
            return;
        }

        var updatedProject = updateResult.project();

        projectCommandRepository.update(updatedProject);
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

    private ProjectUpdateResult changeStatus(Project project, ProjectStatus newProjectStatus) {
        return switch (newProjectStatus) {
            case ACTIVE -> project.restore();
            case ARCHIVED -> project.archive();
        };
    }

}
