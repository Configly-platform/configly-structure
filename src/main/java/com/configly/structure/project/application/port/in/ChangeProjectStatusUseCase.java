package com.configly.structure.project.application.port.in;

import com.configly.structure.project.application.port.in.command.ChangeProjectStatusCommand;

public interface ChangeProjectStatusUseCase {

    void handle(ChangeProjectStatusCommand command);

}
