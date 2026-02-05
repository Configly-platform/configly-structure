package pl.feature.toggle.service.configuration.project.application.port.out;

import pl.feature.toggle.service.configuration.project.domain.Project;
import pl.feature.toggle.service.configuration.project.domain.ProjectUpdateResult;

public interface ProjectCommandRepository {

    void save(Project project);

    void update(ProjectUpdateResult updateResult);
}
