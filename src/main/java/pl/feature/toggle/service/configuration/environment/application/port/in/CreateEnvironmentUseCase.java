package pl.feature.toggle.service.configuration.environment.application.port.in;


import pl.feature.toggle.service.model.environment.EnvironmentId;

public interface CreateEnvironmentUseCase {

    EnvironmentId handle(final CreateEnvironmentCommand command);

}
