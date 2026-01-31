package pl.feature.toggle.service.configuration.environment.application.port.in;

import pl.feature.toggle.service.configuration.environment.application.port.in.command.ChangeEnvironmentTypeCommand;

public interface ChangeEnvironmentTypeUseCase {

    void handle(ChangeEnvironmentTypeCommand command);
}
