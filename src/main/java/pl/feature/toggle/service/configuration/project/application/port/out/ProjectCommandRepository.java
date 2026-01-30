package pl.feature.toggle.service.configuration.project.application.port.out;

import pl.feature.toggle.service.configuration.project.domain.Project;

public interface ProjectCommandRepository {

    void save(Project project);

    void update(Project project);
}
