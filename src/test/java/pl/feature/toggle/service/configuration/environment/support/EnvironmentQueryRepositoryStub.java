package pl.feature.toggle.service.configuration.environment.support;

import pl.feature.toggle.service.configuration.StubSupport;
import pl.feature.toggle.service.configuration.environment.application.port.out.EnvironmentQueryRepository;
import pl.feature.toggle.service.configuration.environment.domain.Environment;
import pl.feature.toggle.service.model.environment.EnvironmentId;
import pl.feature.toggle.service.model.environment.EnvironmentName;
import pl.feature.toggle.service.model.project.ProjectId;

import static pl.feature.toggle.service.configuration.StubSupport.forMethod;

public class EnvironmentQueryRepositoryStub implements EnvironmentQueryRepository {

    private final StubSupport<Boolean> existByProjectIdAndName = forMethod("existsByProjectIdAndName(ProjectId, EnvironmentName)");
    private final StubSupport<Environment> getOrThrow = forMethod("getOrThrow(EnvironmentId, ProjectId)");

    public void existsByProjectIdAndNameReturns(boolean value) {
        existByProjectIdAndName.willReturn(value);
    }

    public void getOrThrowReturns(Environment environment) {
        getOrThrow.willReturn(environment);
    }

    public void getOrThrowThrows(RuntimeException ex) {
        getOrThrow.willThrow(ex);
    }

    public void reset() {
        existByProjectIdAndName.reset();
        getOrThrow.reset();
    }

    @Override
    public Environment getOrThrow(EnvironmentId environmentId, ProjectId projectId) {
        return getOrThrow.get();
    }

    @Override
    public boolean existsByProjectIdAndName(ProjectId projectId, EnvironmentName environmentName) {
        return existByProjectIdAndName.get();
    }
}
