package com.configly.structure.project.support;

import com.configly.structure.StubSupport;
import com.configly.structure.project.application.port.out.ProjectQueryRepository;
import com.configly.structure.project.domain.Project;
import com.configly.model.project.ProjectId;
import com.configly.model.project.ProjectStatus;

import static com.configly.structure.StubSupport.forMethod;

public class ProjectQueryRepositoryStub implements ProjectQueryRepository {

    private final StubSupport<Boolean> exists = forMethod("exists(ProjectId)");
    private final StubSupport<Project> getOrThrow = forMethod("getOrThrow(ProjectId)");
    private final StubSupport<ProjectStatus> fetchStatus = forMethod("fetchStatus(ProjectId)");

    public void existsReturns(boolean value) {
        exists.willReturn(value);
    }

    public void getOrThrowReturns(Project project) {
        getOrThrow.willReturn(project);
    }

    public void getOrThrowThrows(RuntimeException ex) {
        getOrThrow.willThrow(ex);
    }

    public void fetchStatusReturns(ProjectStatus status) {
        fetchStatus.willReturn(status);
    }

    public void fetchStatusThrows(RuntimeException ex) {
        fetchStatus.willThrow(ex);
    }

    public void reset() {
        exists.reset();
        getOrThrow.reset();
        fetchStatus.reset();
    }

    @Override
    public boolean exists(ProjectId projectId) {
        return exists.get();
    }

    @Override
    public Project getOrThrow(ProjectId projectId) {
        return getOrThrow.get();
    }

    @Override
    public ProjectStatus fetchStatus(ProjectId projectId) {
        return fetchStatus.get();
    }
}
