package com.configly.structure.project.application.port.out;

import com.configly.structure.project.domain.Project;
import com.configly.model.project.ProjectId;
import com.configly.model.project.ProjectStatus;

public interface ProjectQueryRepository {

    boolean exists(ProjectId projectId);

    Project getOrThrow(ProjectId projectId);

    ProjectStatus fetchStatus(ProjectId projectId);

}
