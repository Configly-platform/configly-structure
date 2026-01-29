package pl.feature.toggle.service.configuration.project.application.port.in;

import pl.feature.toggle.service.configuration.project.application.port.in.command.ChangeStatusCommand;

public interface ChangeProjectStatusUseCase {

    void handle(ChangeStatusCommand command);

}
