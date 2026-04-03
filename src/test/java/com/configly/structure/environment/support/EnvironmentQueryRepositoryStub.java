package com.configly.structure.environment.support;

import com.configly.structure.StubSupport;
import com.configly.structure.environment.application.port.out.EnvironmentQueryRepository;
import com.configly.structure.environment.domain.Environment;
import com.configly.model.environment.EnvironmentId;
import com.configly.model.project.ProjectId;

import static com.configly.structure.StubSupport.forMethod;

public class EnvironmentQueryRepositoryStub implements EnvironmentQueryRepository {

    private final StubSupport<Environment> getOrThrow = forMethod("getOrThrow(ProjectId, EnvironmentId)");


    public void getOrThrowReturns(Environment environment) {
        getOrThrow.willReturn(environment);
    }

    public void getOrThrowThrows(RuntimeException ex) {
        getOrThrow.willThrow(ex);
    }

    public void reset() {
        getOrThrow.reset();
    }

    @Override
    public Environment getOrThrow(ProjectId projectId, EnvironmentId environmentId) {
        return getOrThrow.get();
    }

}
