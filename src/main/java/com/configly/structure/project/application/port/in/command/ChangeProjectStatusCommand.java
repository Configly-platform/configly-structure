package com.configly.structure.project.application.port.in.command;

import com.configly.model.project.ProjectId;
import com.configly.model.project.ProjectStatus;
import com.configly.web.actor.Actor;
import com.configly.web.correlation.CorrelationId;

public record ChangeProjectStatusCommand(
        ProjectId projectId,
        ProjectStatus newProjectStatus,
        Actor actor,
        CorrelationId correlationId
) {
    public static ChangeProjectStatusCommand of(String projectId, String status, Actor actor, CorrelationId correlationId) {
        return new ChangeProjectStatusCommand(ProjectId.create(projectId), ProjectStatus.valueOf(status),
                actor, correlationId);
    }
}
