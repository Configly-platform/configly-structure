package com.configly.structure.environment.application.port.out;

import com.configly.structure.environment.domain.Environment;
import com.configly.model.environment.EnvironmentId;
import com.configly.model.project.ProjectId;

public interface EnvironmentQueryRepository {

    Environment getOrThrow(ProjectId environmentId, EnvironmentId projectId);
}
