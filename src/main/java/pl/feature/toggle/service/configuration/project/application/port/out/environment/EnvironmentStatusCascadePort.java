package pl.feature.toggle.service.configuration.project.application.port.out.environment;

import pl.feature.toggle.service.model.project.ProjectId;

import java.util.List;

public interface EnvironmentStatusCascadePort {

    List<CascadedEnvironmentStatusChange> archiveCascadeByProjectId(ProjectId projectId);

}
