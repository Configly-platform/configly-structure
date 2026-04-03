package com.configly.structure.environment.application.port.in;


import com.configly.structure.environment.application.port.in.command.ChangeEnvironmentStatusCommand;

public interface ChangeEnvironmentStatusUseCase {

    void handle(ChangeEnvironmentStatusCommand command);

}
