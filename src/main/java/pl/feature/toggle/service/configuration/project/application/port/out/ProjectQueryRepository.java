package pl.feature.toggle.service.configuration.project.application.port.out;

import pl.feature.toggle.service.configuration.project.domain.Project;
import pl.feature.toggle.service.model.project.ProjectId;
import pl.feature.toggle.service.model.project.ProjectName;
import pl.feature.toggle.service.model.project.ProjectStatus;

public interface ProjectQueryRepository {

    boolean exists(ProjectId projectId);

    Project getOrThrow(ProjectId projectId);

    ProjectStatus fetchStatus(ProjectId projectId);

}
