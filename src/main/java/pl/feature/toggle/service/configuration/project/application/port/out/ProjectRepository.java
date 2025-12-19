package pl.feature.toggle.service.configuration.project.application.port.out;

import com.ftaas.domain.project.ProjectName;
import pl.feature.toggle.service.configuration.project.domain.Project;
import com.ftaas.domain.project.ProjectId;

import java.util.Optional;

public interface ProjectRepository {

    ProjectId save(Project project);

    boolean exists(ProjectId projectId);

    boolean existsByName(ProjectName name);

    Optional<Project> findById(ProjectId projectId);
}
