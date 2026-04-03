package com.configly.structure.environment.infrastructure.in.internal;

import lombok.AllArgsConstructor;
import com.configly.structure.environment.application.port.out.EnvironmentCommandRepository;
import com.configly.structure.project.application.port.out.environment.CascadedEnvironmentStatusChange;
import com.configly.structure.project.application.port.out.environment.EnvironmentStatusCascadePort;
import com.configly.model.project.ProjectId;

import java.util.List;

@AllArgsConstructor
class EnvironmentStatusCascadeAdapter implements EnvironmentStatusCascadePort {

    private final EnvironmentCommandRepository environmentCommandRepository;

    @Override
    public List<CascadedEnvironmentStatusChange> archiveCascadeByProjectId(ProjectId projectId) {
        return environmentCommandRepository.archiveAllByProjectId(projectId);
    }

}
