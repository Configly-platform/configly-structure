package pl.feature.toggle.service.configuration.project.application.handler;

import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import pl.feature.toggle.service.configuration.project.application.port.in.ChangeProjectStatusUseCase;
import pl.feature.toggle.service.configuration.project.application.port.in.command.ChangeProjectStatusCommand;
import pl.feature.toggle.service.configuration.project.application.port.out.ProjectCommandRepository;
import pl.feature.toggle.service.configuration.project.application.port.out.ProjectQueryRepository;
import pl.feature.toggle.service.configuration.project.application.port.out.environment.CascadedEnvironmentStatusChange;
import pl.feature.toggle.service.configuration.project.application.port.out.environment.EnvironmentStatusCascadePort;
import pl.feature.toggle.service.configuration.project.domain.Project;
import pl.feature.toggle.service.configuration.project.domain.ProjectUpdateResult;
import pl.feature.toggle.service.model.project.ProjectStatus;
import pl.feature.toggle.service.model.security.actor.Actor;
import pl.feature.toggle.service.model.security.actor.ActorProvider;
import pl.feature.toggle.service.model.security.correlation.CorrelationId;
import pl.feature.toggle.service.model.security.correlation.CorrelationProvider;
import pl.feature.toggle.service.outbox.api.OutboxWriter;

import java.util.Collections;
import java.util.List;

import static java.util.Collections.emptyList;
import static pl.feature.toggle.service.configuration.project.application.handler.EventMapper.createEnvironmentStatusChanged;
import static pl.feature.toggle.service.configuration.project.application.handler.EventMapper.createProjectStatusChangedEvent;
import static pl.feature.toggle.service.contracts.topic.KafkaTopic.CONFIGURATION;

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

        projectCommandRepository.update(updateResult);
        var cascadedChanges = changeEnvironmentsStatus(updateResult.project());

        var actor = actorProvider.current();
        var correlation = correlationProvider.current();

        sendProjectStatusChangedEvent(updateResult, actor, correlation);
        sendEnvironmentStatusChangedEvent(cascadedChanges, actor, correlation);
    }

    private List<CascadedEnvironmentStatusChange> changeEnvironmentsStatus(Project project) {
        if (project.isArchived()) {
            return environmentStatusCascadePort.archiveCascadeByProjectId(project.id());
        }
        return emptyList();
    }

    private ProjectUpdateResult changeStatus(Project project, ProjectStatus newProjectStatus) {
        return switch (newProjectStatus) {
            case ACTIVE -> project.restore();
            case ARCHIVED -> project.archive();
        };
    }

    private void sendEnvironmentStatusChangedEvent(List<CascadedEnvironmentStatusChange> cascadedChanges, Actor actor, CorrelationId correlation) {
        cascadedChanges.forEach(change -> {
            var environmentEvent = createEnvironmentStatusChanged(change, actor, correlation);
            outboxWriter.write(environmentEvent, CONFIGURATION.topic());
        });
    }

    private void sendProjectStatusChangedEvent(ProjectUpdateResult updateResult, Actor actor, CorrelationId correlation) {
        var projectEvent = createProjectStatusChangedEvent(updateResult, actor, correlation);
        outboxWriter.write(projectEvent, CONFIGURATION.topic());
    }

}
