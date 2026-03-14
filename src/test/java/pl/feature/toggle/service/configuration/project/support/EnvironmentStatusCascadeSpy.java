package pl.feature.toggle.service.configuration.project.support;

import pl.feature.toggle.service.configuration.project.application.port.out.environment.CascadedEnvironmentStatusChange;
import pl.feature.toggle.service.configuration.project.application.port.out.environment.EnvironmentStatusCascadePort;
import pl.feature.toggle.service.model.project.ProjectId;

import java.util.ArrayList;
import java.util.List;

public class EnvironmentStatusCascadeSpy implements EnvironmentStatusCascadePort {

    private List<ProjectId> archived = new ArrayList<>();
    private List<CascadedEnvironmentStatusChange> archiveCascade = new ArrayList<>();
    private boolean failOnAnyCall;

    public void reset() {
        archived.clear();
        archiveCascade.clear();
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

    public void archiveCascadeByProjectIdReturns(List<CascadedEnvironmentStatusChange> changes) {
        archiveCascade.addAll(changes);
    }

    @Override
    public List<CascadedEnvironmentStatusChange> archiveCascadeByProjectId(ProjectId projectId) {
        if (failOnAnyCall) {
            throw new AssertionError("this method should not be called");
        }
        archived.add(projectId);
        return archiveCascade;
    }

}
