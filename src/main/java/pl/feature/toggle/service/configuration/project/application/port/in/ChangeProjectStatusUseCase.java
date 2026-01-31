package pl.feature.toggle.service.configuration.project.application.port.in;

import pl.feature.toggle.service.configuration.project.application.port.in.command.ChangeProjectStatusCommand;

public interface ChangeProjectStatusUseCase {

    void handle(ChangeProjectStatusCommand command);

}
