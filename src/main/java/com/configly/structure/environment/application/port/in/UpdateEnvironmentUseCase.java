package com.configly.structure.environment.application.port.in;


import com.configly.structure.environment.application.port.in.command.UpdateEnvironmentCommand;

public interface UpdateEnvironmentUseCase {

    void handle(UpdateEnvironmentCommand command);

}
