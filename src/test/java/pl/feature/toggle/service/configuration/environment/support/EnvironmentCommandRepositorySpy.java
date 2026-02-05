package pl.feature.toggle.service.configuration.environment.support;

import pl.feature.toggle.service.configuration.environment.application.port.out.EnvironmentCommandRepository;
import pl.feature.toggle.service.configuration.environment.domain.Environment;
import pl.feature.toggle.service.configuration.environment.domain.EnvironmentUpdateResult;
import pl.feature.toggle.service.model.environment.EnvironmentId;
import pl.feature.toggle.service.model.project.ProjectId;

import java.util.ArrayList;
import java.util.List;

public class EnvironmentCommandRepositorySpy implements EnvironmentCommandRepository {

    private final List<Environment> saved = new ArrayList<>();
    private final List<Environment> updated = new ArrayList<>();
    private final List<ProjectId> archivedAll = new ArrayList<>();
    private final List<ProjectId> restoredAll = new ArrayList<>();
    private boolean failOnAnyCall;

    public void reset() {
        saved.clear();
        updated.clear();
        archivedAll.clear();
        restoredAll.clear();
        failOnAnyCall = false;
    }

    public Environment getSaved() {
        if (saved.isEmpty()) {
            return null;
        }
        return saved.getLast();
    }

    public void expectNoCalls() {
        failOnAnyCall = true;
    }

    public Environment getUpdated() {
        if (updated.isEmpty()) {
            return null;
        }
        return updated.getLast();
    }

    public ProjectId getLastArchivedAll() {
        return archivedAll.getLast();
    }

    public ProjectId getLastRestoredAll() {
        return restoredAll.getLast();
    }

    @Override
    public EnvironmentId save(Environment environment) {
        if (failOnAnyCall) {
            throw new AssertionError("this method should not be called");
        }
        saved.add(environment);
        return environment.id();
    }

    @Override
    public void archiveAllByProjectId(ProjectId projectId) {
        if (failOnAnyCall) {
            throw new AssertionError("this method should not be called");
        }
        archivedAll.add(projectId);
    }

    @Override
    public void restoreAllByProjectId(ProjectId projectId) {
        if (failOnAnyCall) {
            throw new AssertionError("this method should not be called");
        }
        restoredAll.add(projectId);
    }

    @Override
    public void update(EnvironmentUpdateResult updateResult) {
        if (failOnAnyCall) {
            throw new AssertionError("this method should not be called");
        }
        updated.add(updateResult.environment());
    }
}
