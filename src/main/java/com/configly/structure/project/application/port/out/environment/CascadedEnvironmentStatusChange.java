package com.configly.structure.project.application.port.out.environment;

import com.configly.model.CreatedAt;
import com.configly.model.Revision;
import com.configly.model.environment.EnvironmentId;
import com.configly.model.environment.EnvironmentStatus;
import com.configly.model.project.ProjectId;

public record CascadedEnvironmentStatusChange(
        EnvironmentId environmentId,
        ProjectId projectId,
        EnvironmentStatus afterStatus,
        EnvironmentStatus beforeStatus,
        Revision revision,
        CreatedAt createdAt
) {
}
