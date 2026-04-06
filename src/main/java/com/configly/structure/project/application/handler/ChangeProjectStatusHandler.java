package com.configly.structure.project.application.handler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import com.configly.structure.project.application.port.in.ChangeProjectStatusUseCase;
import com.configly.structure.project.application.port.in.command.ChangeProjectStatusCommand;
import com.configly.structure.project.application.port.out.ProjectCommandRepository;
import com.configly.structure.project.application.port.out.ProjectQueryRepository;
import com.configly.structure.project.application.port.out.environment.CascadedEnvironmentStatusChange;
import com.configly.structure.project.application.port.out.environment.EnvironmentStatusCascadePort;
import com.configly.structure.project.domain.Project;
import com.configly.structure.project.domain.ProjectUpdateResult;
import com.configly.model.project.ProjectStatus;
import com.configly.outbox.api.OutboxEvent;
import com.configly.web.model.actor.Actor;
import com.configly.web.model.correlation.CorrelationId;
import com.configly.outbox.api.OutboxWriter;

import java.util.List;

import static java.util.Collections.emptyList;
import static com.configly.structure.project.application.handler.EventMapper.createEnvironmentStatusChanged;
import static com.configly.structure.project.application.handler.EventMapper.createProjectStatusChangedEvent;
import static com.configly.contracts.topic.KafkaTopic.CONFIGURATION;

@AllArgsConstructor
@Slf4j
class ChangeProjectStatusHandler implements ChangeProjectStatusUseCase {

    private final ProjectCommandRepository projectCommandRepository;
    private final ProjectQueryRepository projectQueryRepository;
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

        sendProjectStatusChangedEvent(updateResult, command.actor(), command.correlationId());
        sendEnvironmentStatusChangedEvent(cascadedChanges, command.actor(), command.correlationId());
        log.info("Project status changed: projectId={}, oldStatus={}, newStatus={}", project.id().uuid(),
                project.status(), updateResult.project().status());
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
            outboxWriter.write(OutboxEvent.of(environmentEvent.projectId().toString(), environmentEvent, CONFIGURATION));
        });
    }

    private void sendProjectStatusChangedEvent(ProjectUpdateResult updateResult, Actor actor, CorrelationId correlation) {
        var projectEvent = createProjectStatusChangedEvent(updateResult, actor, correlation);
        outboxWriter.write(OutboxEvent.of(projectEvent.projectId().toString(), projectEvent, CONFIGURATION));
    }

}
