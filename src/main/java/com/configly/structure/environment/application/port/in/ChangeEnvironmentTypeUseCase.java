package com.configly.structure.environment.application.port.in;

import com.configly.structure.environment.application.port.in.command.ChangeEnvironmentTypeCommand;

public interface ChangeEnvironmentTypeUseCase {

    void handle(ChangeEnvironmentTypeCommand command);
}
