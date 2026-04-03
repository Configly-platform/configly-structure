package com.configly.structure.project.application.port.in.command;

import com.configly.structure.project.infrastructure.in.rest.dto.ProjectSnapshotDto;
import com.configly.model.project.ProjectDescription;
import com.configly.model.project.ProjectName;
import com.configly.web.actor.Actor;
import com.configly.web.correlation.CorrelationId;

public record CreateProjectCommand(
        ProjectName name,
        ProjectDescription description,
        Actor actor,
        CorrelationId correlationId
) {

    public static CreateProjectCommand from(ProjectSnapshotDto dto, Actor actor, CorrelationId correlationId) {
        return new CreateProjectCommand(ProjectName.create(dto.name()), ProjectDescription.create(dto.description()),
                actor, correlationId);
    }

}
