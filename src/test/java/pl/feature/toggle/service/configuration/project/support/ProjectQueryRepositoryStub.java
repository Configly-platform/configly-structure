package pl.feature.toggle.service.configuration.project.support;

import pl.feature.toggle.service.configuration.StubSupport;
import pl.feature.toggle.service.configuration.project.application.port.out.ProjectQueryRepository;
import pl.feature.toggle.service.configuration.project.domain.Project;
import pl.feature.toggle.service.model.project.ProjectId;
import pl.feature.toggle.service.model.project.ProjectName;
import pl.feature.toggle.service.model.project.ProjectStatus;

import static pl.feature.toggle.service.configuration.StubSupport.forMethod;

public class ProjectQueryRepositoryStub implements ProjectQueryRepository {

    private final StubSupport<Boolean> exists = forMethod("exists(ProjectId)");
    private final StubSupport<Boolean> existsByName = forMethod("existsByName(ProjectName)");
    private final StubSupport<Project> getOrThrow = forMethod("getOrThrow(ProjectId)");
    private final StubSupport<ProjectStatus> fetchStatus = forMethod("fetchStatus(ProjectId)");

    public void existsReturns(boolean value) {
        exists.willReturn(value);
    }

    public void existsByNameReturns(boolean value) {
        existsByName.willReturn(value);
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
        existsByName.reset();
        getOrThrow.reset();
        fetchStatus.reset();
    }

    @Override
    public boolean exists(ProjectId projectId) {
        return exists.get();
    }

    @Override
    public boolean existsByName(ProjectName name) {
        return existsByName.get();
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
