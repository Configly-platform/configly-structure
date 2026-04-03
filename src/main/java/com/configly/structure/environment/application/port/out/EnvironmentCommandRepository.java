package com.configly.structure.environment.application.port.out;

import com.configly.structure.environment.domain.Environment;
import com.configly.structure.environment.domain.EnvironmentUpdateResult;
import com.configly.structure.project.application.port.out.environment.CascadedEnvironmentStatusChange;
import com.configly.model.environment.EnvironmentId;
import com.configly.model.project.ProjectId;

import java.util.List;

public interface EnvironmentCommandRepository {

    EnvironmentId save(Environment environment);

    List<CascadedEnvironmentStatusChange> archiveAllByProjectId(ProjectId projectId);

    void update(EnvironmentUpdateResult updateResult);
}
