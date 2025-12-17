package pl.feature.toggle.service.project.application.port.in;

import com.ftaas.domain.project.ProjectId;

public interface CreateProjectUseCase {

    ProjectId handle(final CreateProjectCommand command);

}
