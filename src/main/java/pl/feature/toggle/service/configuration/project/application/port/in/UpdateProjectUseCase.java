package pl.feature.toggle.service.configuration.project.application.port.in;

import pl.feature.toggle.service.configuration.project.application.port.in.command.UpdateProjectCommand;

public interface UpdateProjectUseCase {

    void handle(UpdateProjectCommand command);

}
