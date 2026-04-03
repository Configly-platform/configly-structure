package com.configly.structure.project.application.port.out.environment;

import com.configly.model.project.ProjectId;

import java.util.List;

public interface EnvironmentStatusCascadePort {

    List<CascadedEnvironmentStatusChange> archiveCascadeByProjectId(ProjectId projectId);

}
