package pl.feature.toggle.service.configuration.project.infrastructure;

import pl.feature.toggle.service.configuration.project.application.port.out.ProjectCommandRepository;
import pl.feature.toggle.service.configuration.project.domain.Project;

import java.util.ArrayList;
import java.util.List;

public class ProjectCommandRepositorySpy implements ProjectCommandRepository {

    private final List<Project> saved = new ArrayList<>();
    private final List<Project> updated = new ArrayList<>();

    private boolean failOnAnyCall;

    @Override
    public void save(Project project) {
        if (failOnAnyCall) {
            throw new AssertionError("this method should not be called");
        }
        saved.add(project);
    }

    @Override
    public void update(Project project) {
        if (failOnAnyCall) {
            throw new AssertionError("this method should not be called");
        }
        updated.add(project);
    }

    public void reset() {
        saved.clear();
        updated.clear();
        failOnAnyCall = false;
    }

    public void failing() {
        failOnAnyCall = true;
    }

    public Project getSaved() {
        return saved.getLast();
    }

    public Project getUpdated() {
        return updated.getLast();
    }
}
