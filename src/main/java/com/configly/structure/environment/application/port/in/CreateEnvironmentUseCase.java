package com.configly.structure.environment.application.port.in;


import com.configly.structure.environment.application.port.in.command.CreateEnvironmentCommand;
import com.configly.model.environment.EnvironmentId;

public interface CreateEnvironmentUseCase {

    EnvironmentId handle(CreateEnvironmentCommand command);

}
