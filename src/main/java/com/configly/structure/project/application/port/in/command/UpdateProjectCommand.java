package com.configly.structure.project.application.port.in.command;

import com.configly.structure.project.infrastructure.in.rest.dto.ProjectSnapshotDto;
import com.configly.model.project.ProjectDescription;
import com.configly.model.project.ProjectId;
import com.configly.model.project.ProjectName;
import com.configly.web.model.actor.Actor;
import com.configly.web.model.correlation.CorrelationId;

public record UpdateProjectCommand(
        ProjectId projectId,
        ProjectName name,
        ProjectDescription description,
        Actor actor,
        CorrelationId correlationId
) {

    public static UpdateProjectCommand from(String projectId, ProjectSnapshotDto dto, Actor actor, CorrelationId correlationId) {
        return new UpdateProjectCommand(ProjectId.create(projectId), ProjectName.create(dto.name()), ProjectDescription.create(dto.description()),
                actor, correlationId);
    }
}
