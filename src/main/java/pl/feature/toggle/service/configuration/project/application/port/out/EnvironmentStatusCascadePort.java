package pl.feature.toggle.service.configuration.project.application.port.out;

import pl.feature.toggle.service.model.project.ProjectId;

public interface EnvironmentStatusCascadePort {

    void archiveCascadeByProjectId(ProjectId projectId);

    void restoreCascadeByProjectId(ProjectId projectId);

}
