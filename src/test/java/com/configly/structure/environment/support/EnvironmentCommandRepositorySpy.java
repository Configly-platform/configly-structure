package com.configly.structure.environment.support;

import com.configly.structure.environment.application.port.out.EnvironmentCommandRepository;
import com.configly.structure.environment.domain.Environment;
import com.configly.structure.environment.domain.EnvironmentUpdateResult;
import com.configly.structure.project.application.port.out.environment.CascadedEnvironmentStatusChange;
import com.configly.model.environment.EnvironmentId;
import com.configly.model.project.ProjectId;

import java.util.ArrayList;
import java.util.List;

public class EnvironmentCommandRepositorySpy implements EnvironmentCommandRepository {

    private final List<Environment> saved = new ArrayList<>();
    private final List<Environment> updated = new ArrayList<>();
    private final List<ProjectId> archivedAll = new ArrayList<>();
    private boolean failOnAnyCall;

    public void reset() {
        saved.clear();
        updated.clear();
        archivedAll.clear();
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

    @Override
    public EnvironmentId save(Environment environment) {
        if (failOnAnyCall) {
            throw new AssertionError("this method should not be called");
        }
        saved.add(environment);
        return environment.id();
    }

    @Override
    public List<CascadedEnvironmentStatusChange> archiveAllByProjectId(ProjectId projectId) {
        if (failOnAnyCall) {
            throw new AssertionError("this method should not be called");
        }
        archivedAll.add(projectId);
        return List.of();
    }

    @Override
    public void update(EnvironmentUpdateResult updateResult) {
        if (failOnAnyCall) {
            throw new AssertionError("this method should not be called");
        }
        updated.add(updateResult.environment());
    }
}
