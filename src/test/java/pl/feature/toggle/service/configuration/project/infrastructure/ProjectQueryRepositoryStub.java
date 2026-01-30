package pl.feature.toggle.service.configuration.project.infrastructure;

import pl.feature.toggle.service.configuration.project.application.port.out.ProjectQueryRepository;
import pl.feature.toggle.service.configuration.project.domain.Project;
import pl.feature.toggle.service.model.project.ProjectId;
import pl.feature.toggle.service.model.project.ProjectName;

import java.util.Optional;

public class ProjectQueryRepositoryStub implements ProjectQueryRepository {

    private Boolean existsResult;
    private Boolean existsByNameResult;
    private Boolean findByIdIsStubbed;
    private Project findByIdResult;

    public void exists(Boolean value) {
        this.existsResult = value;
    }

    public void existsByName(Boolean value) {
        this.existsByNameResult = value;
    }

    public void returns(Project project) {
        this.findByIdResult = project;
        this.findByIdIsStubbed = true;
    }

    public void notFound() {
        this.findByIdResult = null;
        this.findByIdIsStubbed = true;
    }

    public void reset() {
        this.existsResult = null;
        this.existsByNameResult = null;
        this.findByIdResult = null;
        this.findByIdIsStubbed = null;
    }


    @Override
    public boolean exists(ProjectId projectId) {
        if (existsResult == null) {
            throw new AssertionError("exists(ProjectId) was not stubbed");
        }
        return existsResult;
    }

    @Override
    public boolean existsByName(ProjectName name) {
        if (existsByNameResult == null) {
            throw new AssertionError("existsByName(ProjectName) was not stubbed");
        }
        return existsByNameResult;
    }

    @Override
    public Optional<Project> findById(ProjectId projectId) {
        if (findByIdIsStubbed == null) {
            throw new AssertionError("findById(ProjectId) was not stubbed");
        }
        return Optional.of(findByIdResult);
    }
}
