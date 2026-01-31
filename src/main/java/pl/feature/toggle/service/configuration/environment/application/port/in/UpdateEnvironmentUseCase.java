package pl.feature.toggle.service.configuration.environment.application.port.in;


import pl.feature.toggle.service.configuration.environment.application.port.in.command.UpdateEnvironmentCommand;
import pl.feature.toggle.service.model.environment.EnvironmentId;

public interface UpdateEnvironmentUseCase {

    void handle(UpdateEnvironmentCommand command);

}
