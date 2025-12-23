package pl.feature.toggle.service.configuration.project.application.port.in;


import pl.feature.toggle.service.model.project.ProjectId;

public interface CreateProjectUseCase {

    ProjectId handle(final CreateProjectCommand command);

}
