package pl.feature.toggle.service.configuration.project.infrastructure;

import com.ftaas.domain.project.ProjectId;
import com.ftaas.domain.project.ProjectName;
import pl.feature.toggle.service.configuration.project.application.port.out.ProjectRepository;
import pl.feature.toggle.service.configuration.project.domain.Project;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FakeProjectRepository implements ProjectRepository {

    private final Map<ProjectId, Project> projects = new HashMap<>();

    @Override
    public ProjectId save(Project project) {
        projects.put(project.id(), project);
        return project.id();
    }

    @Override
    public boolean exists(ProjectId projectId) {
        return projects.containsKey(projectId);
    }

    @Override
    public boolean existsByName(ProjectName name) {
        return projects.values().stream().anyMatch(project -> project.name().equals(name));
    }

    @Override
    public Optional<Project> findById(ProjectId projectId) {
        return Optional.ofNullable(projects.get(projectId));
    }

    public void clear() {
        projects.clear();
    }

}
