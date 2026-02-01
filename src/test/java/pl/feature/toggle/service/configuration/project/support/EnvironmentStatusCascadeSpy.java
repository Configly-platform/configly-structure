package pl.feature.toggle.service.configuration.project.support;

import pl.feature.toggle.service.configuration.project.application.port.out.EnvironmentStatusCascadePort;
import pl.feature.toggle.service.model.project.ProjectId;

import java.util.ArrayList;
import java.util.List;

public class EnvironmentStatusCascadeSpy implements EnvironmentStatusCascadePort {

    private List<ProjectId> archived = new ArrayList<>();
    private List<ProjectId> restored = new ArrayList<>();
    private boolean failOnAnyCall;

    public void reset() {
        archived.clear();
        restored.clear();
        failOnAnyCall = false;
    }

    public void expectNoCalls() {
        failOnAnyCall = true;
    }

    public ProjectId getLastArchived() {
        if (archived.isEmpty()) {
            return null;
        }
        return archived.getLast();
    }

    public ProjectId getLastRestored() {
        if (restored.isEmpty()) {
            return null;
        }
        return restored.getLast();
    }

    @Override
    public void archiveCascadeByProjectId(ProjectId projectId) {
        if (failOnAnyCall) {
            throw new AssertionError("this method should not be called");
        }
        archived.add(projectId);
    }

    @Override
    public void restoreCascadeByProjectId(ProjectId projectId) {
        if (failOnAnyCall) {
            throw new AssertionError("this method should not be called");
        }
        restored.add(projectId);
    }
}
