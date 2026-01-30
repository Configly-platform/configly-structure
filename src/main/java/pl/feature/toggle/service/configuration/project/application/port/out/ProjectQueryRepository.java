package pl.feature.toggle.service.configuration.project.application.port.out;

import pl.feature.toggle.service.configuration.project.domain.Project;
import pl.feature.toggle.service.model.project.ProjectId;
import pl.feature.toggle.service.model.project.ProjectName;

import java.util.Optional;

public interface ProjectQueryRepository {

    boolean exists(ProjectId projectId);

    boolean existsByName(ProjectName name);

    Optional<Project> findById(ProjectId projectId);

}
